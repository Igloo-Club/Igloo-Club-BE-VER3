package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.*;
import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import com.igloo_club.nungil_v3.dto.NungilDetailResponse;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerResponse;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.NungilErrorResult;
import com.igloo_club.nungil_v3.repository.BlockedMemberRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import com.igloo_club.nungil_v3.repository.NungilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NungilService {
    private final PresignedUrlService presignedUrlService;

    private final QuestionAndAnswerService questionAndAnswerService;

    private final MemberRepository memberRepository;

    private final BlockedMemberRepository blockedMemberRepository;

    private final NungilRepository nungilRepository;

    private static final Long RECOMMENDATION_LIMIT = 1L;
    /* 눈길 관리 */
    /**
     * 사용자를 추천하는 api입니다.
     *
     * @request member
     * @return nungilResponse 추천되는 사용자 눈길 정보
     */
    @Transactional
public NungilResponse recommendMember(Member member){

        // 1. 하루 제한 횟수를 초과한 경우, 예외를 발생시킨다.
        if (checkLimitExcess(member)) {
            throw new GeneralException(NungilErrorResult.LIMIT_EXCEEDED);
        }

        // 2. 무료 뽑기 가능한 시간대가 아닌 경우, 예외를 발생시킨다.
        if (checkTimeOut()) {
            throw new GeneralException(NungilErrorResult.OUT_OF_TIME);
        }

        // 3. 회원 한 명을 추천받는다.
        Member recommendedMember = getRecommendedMember(member);
        if (recommendedMember == null) {
            throw new GeneralException(NungilErrorResult.NO_RECOMMENDATION);
        }

        // 4. 추천 받은 회원에 대한 차단(중복방지)를 생성하고 저장한다.
        BlockedMember blockedMember = getBlockedMember(member, recommendedMember);
        blockedMemberRepository.save(blockedMember);

        // 5. 추천 받은 회원에 대한 눈길을 생성하고 저장한다.
        Nungil newNungil = Nungil.create(member, recommendedMember, NungilStatus.RECOMMENDED);
        nungilRepository.save(newNungil);

        // 6. 추천이 정상적으로 동작했을 시 drawCount를 1 증가 시킨다.
        member.plusDrawCount();

        // 7. 추천 받은 회원 정보를 반환한다.
        List<String> imageUrlList = getImageUrlList(recommendedMember);
        return NungilResponse.create(newNungil, imageUrlList);
    }

    private Member getRecommendedMember(Member currentMember) {
        List<Member> membersList = memberRepository.findBySex(currentMember.getOppositeSex());
        List<BlockedMember> blockedMembersList = blockedMemberRepository.findByMember(currentMember);

        List<Long> blockedMemberIds = blockedMembersList.stream()
                .map(blockedMember -> blockedMember.getOpponent().getId())
                .collect(Collectors.toList());

        // Block된 멤버와 위치를 기준으로 membersList 필터링
        membersList = membersList.stream()
                .filter(member -> !blockedMemberIds.contains(member.getId())) // Block된 멤버 제외
                .filter(member -> member.getLocation().stream()
                        .anyMatch(location -> currentMember.getLocation().contains(location))) // location 비교
                .collect(Collectors.toList());

        Ideal ideal = currentMember.getIdeal();
        Map<Member, Integer> similarityMap = new HashMap<>(); // 각 멤버와 선호를 저장할 맵

        for (Member member : membersList){
            int similarityCount = 0;

            // 나이 비교
            int memberAge = member.calculateAge();
            if (memberAge >= ideal.getPreferredAgeStart() && memberAge <= ideal.getPreferredAgeEnd()) {
                similarityCount++;
            }

            // 키 비교
            Profile profile = member.getProfile();
            if (profile != null) {
                int memberHeight = profile.getHeight();
                if (memberHeight >= ideal.getPreferredHeightStart() && memberHeight <= ideal.getPreferredHeightEnd()) {
                    similarityCount++;
                }
            }

            // MBTI 비교
            if (ideal.getMbtiList().contains(member.getProfile().getMbtiType())) {
                similarityCount++;
            }

            // 흡연 여부 비교
            if (ideal.getSmoke().equals(member.getProfile().getSmoke())) {
                similarityCount++;
            }

            // 종교 비교
            if (ideal.getReligion() == null || ideal.getReligion().equals(member.getProfile().getReligion())) {
                similarityCount++;
            }

            // 결혼 계획 비교
            if (ideal.getMarriagePlan() == null || ideal.getMarriagePlan().equals(member.getProfile().getMarriagePlan())) {
                similarityCount++;
            }

            // 맵에 추가
            similarityMap.put(member, similarityCount);

        }
        List<Member> recommendingMembersList = similarityMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (recommendingMembersList.isEmpty()) {
            // 추천할 멤버가 없을 시 null 반환
            return null;
        }
        // 랜덤한 멤버 ID 선택
        Random random = new Random();
        Member recommendedMember = recommendingMembersList.get(random.nextInt(recommendingMembersList.size()));

        // 선택된 멤버 정보 가져오기
        return recommendedMember;
    }

    /**
     * 사용자 추천 제한 횟수를 초과했는지 확인하는 메서드이다.
     * @param member 제한 횟수 초과를 확인할 회원
     * @return 초과한 경우 true, 제한 횟수가 남은 경우 false
     */
    private boolean checkLimitExcess(Member member) {
        Long count = member.getDrawCount();
        return RECOMMENDATION_LIMIT <= count;
    }

    /**
     * 사용자 무료 추천 시간대인지 확인하는 메서드이다.
     * 11시 ~ 13시, 18시 ~ 20시 이외일 시 예외 발생
     *
     * @return 초과한 경우 true, 제한 횟수가 남은 경우 false
     */
    private boolean checkTimeOut() {
        int whatHour = LocalDateTime.now().getHour();
        if ((whatHour>= 11 && whatHour <=13)||(whatHour>= 18 && whatHour <=20)){return true;}
        return false;
    }

    /**
     * 뽑기 횟수를 초기화하는 메서드이다.
     * 매일 11시, 18시에 발생
     *
     * @return 초과한 경우 true, 제한 횟수가 남은 경우 false
     */
    @Scheduled(cron = "0 0 11,18 * * *")
    @Transactional
    public void resetDrawCountForAllMembers() {
        List<Member> allMembers = memberRepository.findAll();
        for (Member member : allMembers) {
            member.resetDrawCount();
        }
    }

    /**
     * BlockedMember 데이터베이스에 member와 opponent로 구성된 데이터가 없는 경우 생성하고, 있는 경우 조회하여 반환하는 메서드이다.
     * @param member Member 엔티티
     * @param opponent member 회원의 차단상대를 가리키는 Member 엔티티
     * @return 조회된 blockedMember 엔티티
     */
    private BlockedMember getBlockedMember(Member member, Member opponent) {
        return blockedMemberRepository.findByMemberAndOpponent(member, opponent)
                .orElse(BlockedMember.create(member, opponent, NungilStatus.RECOMMENDED));
    }


    /**
     * 요청 눈길 상태의 프로필을 전체 조회하는 메서드이다.
     *
     * @pararm pagealbe 페이지 정보
     * @param status 요청 눈길 상태
     *
     * @return NungilPageResponse 슬라이스 정보 반환
     */
    public Slice<NungilResponse> getNungilSliceByMemberAndStatus(Member member, NungilStatus status, Pageable pageable){
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Nungil 엔티티를 데이터베이스에서 조회
        Slice<Nungil> nungilSlice = nungilRepository.findAllByMemberAndStatus(pageRequest, member, status);

        // Nungil 엔티티를 NungilPageResponse DTO로 변환

        List<NungilResponse> nungilResponses = nungilSlice.getContent().stream()
                .map(nungil -> NungilResponse.create(nungil,getImageUrlList(nungil.getOpponent())))
                .collect(Collectors.toList());

        // 변환된 DTO 리스트와 함께 새로운 Slice 객체를 생성하여 반환
        return new SliceImpl<>(nungilResponses, pageRequest, nungilSlice.hasNext());
    }

    /**
     * 눈길을 보내는 api입니다
     * member에게 recommend status의 눈길을 SENT로 수정하며
     * opponent에게 status가 RECEIVED인 눈길을 생성합니다
     *
     * @param nungilId 눈길 id
     */
    @Transactional
    public void sendNungil(Member member, Long nungilId) {
        Nungil nungil = nungilRepository.findById(nungilId)
                .orElseThrow(() -> new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        Member opponent = nungil.getOpponent();
        // 눈길 상태가 RECOMMENDED가 아닌 경우 예외 처리
        if (!nungil.getStatus().equals(NungilStatus.RECOMMENDED)) {
            throw new GeneralException(NungilErrorResult.NUNGIL_WRONG_STATUS);
        }

        // BlockedMember에 각각 추가
        BlockedMember memberAcquaintance = getBlockedMember(member, opponent);
        BlockedMember opponentAcquaintance = getBlockedMember(opponent, member);


        // 이미 눈길을 전송했을 시 예외 처리
        List<Nungil> opponentNungilList = nungilRepository.findAllByMemberAndOpponentAndStatus(opponent, member, NungilStatus.RECEIVED);
        if (opponentNungilList.size() > 0) {
            throw new GeneralException(NungilErrorResult.NUNGIL_ALREADY_SENT);
        }

        // member의 눈길 상태를 SENT, 만료일을 3일 뒤로 설정
        nungil.setStatus(NungilStatus.SENT);
        nungil.setExpiredAtDaysAfter(3);
        memberAcquaintance.update(NungilStatus.SENT, 7);

        // opponent의 눈길 생성 및 저장, 만료일을 3일 뒤로 설정
        Nungil newNungil = Nungil.create(opponent, member, NungilStatus.RECEIVED);
        newNungil.setExpiredAtDaysAfter(3);
        opponentAcquaintance.update(NungilStatus.RECEIVED, 7);
        blockedMemberRepository.save(opponentAcquaintance);
        nungilRepository.save(newNungil);
    }

    /**
     * 눈길을 1차 승낙하는 api입니다
     * member의 RECEIVED 눈길을 ACCEPTED_RECEIVED 눈길로 수정하며
     * opponent의 SENT 눈길을 ACCEPTED_SENT 눈길로 수정합니다
     *
     * @param nungilId 눈길 id
     */
    @Transactional
    public void acceptNungil(Member member, Long nungilId) {
        Nungil memberNungil = nungilRepository.findById(nungilId)
                .orElseThrow(()->new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        //눈길이 잘못된 상태일 시 에러 발생
        if(!memberNungil.getStatus().equals(NungilStatus.RECEIVED)){
            throw new GeneralException(NungilErrorResult.NUNGIL_WRONG_STATUS);
        }

        //사용자의 눈길을 ACCEPTED_RECEIVED 상태로 변경
        memberNungil.setStatus(NungilStatus.ACCEPTED_RECEIVED);
        memberNungil.setExpiredAtDaysAfter(3);

        //상대방의 눈길을 ACCEPTED_SENT 상태로 변경
        Member opponent = memberNungil.getOpponent();
        Optional<Nungil> opponentNungilList = nungilRepository.findByMemberAndOpponentAndStatus(opponent, member, NungilStatus.SENT);
        if(opponentNungilList.isEmpty()){
            throw new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND);
        }
        Nungil opponentNungil = opponentNungilList.get();
        opponentNungil.setStatus(NungilStatus.ACCEPTED_SENT);
        opponentNungil.setExpiredAtDaysAfter(3);

        // 서로에 대한 blockedMember 객체 만료 일자 3일 연장
        BlockedMember acquaintanceFromMember = getBlockedMember(member, opponent);
        acquaintanceFromMember.update(NungilStatus.ACCEPTED_RECEIVED, 7);
        blockedMemberRepository.save(acquaintanceFromMember);

        BlockedMember acquaintanceFromOpponent = getBlockedMember(opponent, member);
        acquaintanceFromOpponent.update(NungilStatus.ACCEPTED_SENT, 7);
        blockedMemberRepository.save(acquaintanceFromOpponent);
    }

    /**
     * 눈길을 최종 승락하는 api입니다
     * member의 ACCEPTED_SENT 눈길을 MATCHED 눈길로 수정하며
     * opponent의 ACCEPTED_RECEIVED 눈길을 MATCHED 눈길로 수정합니다
     * 채팅방을 개설합니다
     *
     * @param nungilId 눈길 id
     */
    @Transactional
    public void matchNungil(Member member, Long nungilId) {
        Nungil memberNungil = nungilRepository.findById(nungilId)
                .orElseThrow(()->new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        //눈길이 잘못된 상태일 시 에러 발생
        if(!memberNungil.getStatus().equals(NungilStatus.ACCEPTED_SENT)){
            throw new GeneralException(NungilErrorResult.NUNGIL_WRONG_STATUS);
        }

        //사용자의 눈길을 MATCHED 상태로 변경
        memberNungil.setStatus(NungilStatus.MATCHED);
        memberNungil.setExpiredAtNull();

        //상대방의 눈길을 MATCHED 상태로 변경
        Member opponent = memberNungil.getOpponent();
        Optional<Nungil> opponentNungilList = nungilRepository.findByMemberAndOpponentAndStatus(opponent, member, NungilStatus.ACCEPTED_RECEIVED);
        if(opponentNungilList.isEmpty()){
            throw new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND);
        }
        Nungil opponentNungil = opponentNungilList.get();
        opponentNungil.setStatus(NungilStatus.MATCHED);
        opponentNungil.setExpiredAtNull();

        // 서로에 대한 blockedMember 객체 만료 일자 무기한 연장
        BlockedMember acquaintanceFromMember = getBlockedMember(member, opponent);
        acquaintanceFromMember.updateToMatched(NungilStatus.MATCHED);
        blockedMemberRepository.save(acquaintanceFromMember);

        BlockedMember acquaintanceFromOpponent = getBlockedMember(opponent, member);
        acquaintanceFromOpponent.updateToMatched(NungilStatus.MATCHED);
        blockedMemberRepository.save(acquaintanceFromOpponent);

        // 채팅방 개설 기능 추가
    }

    /**
     * 특정 눈길 정보를 조회하는 api입니다
     *
     * @param nungilId 눈길 id
     * @return nungilDetailResponse 특정 눈길 상세 정보
     */
    public NungilDetailResponse getNungilDetail(Long nungilId){
        Nungil nungil = nungilRepository.findById(nungilId)
                .orElseThrow(() -> new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<QuestionAndAnswerResponse> qaList = questionAndAnswerService.getExposingQuestionAndAnswerPageByMember(nungil.getOpponent(), pageRequest).toList();
        List<QuestionAndAnswerResponse> myQaList = questionAndAnswerService.getExposingQuestionAndAnswerPageByMember(nungil.getMember(), pageRequest).toList();
        int myAnsweredQa = myQaList.size();
        NungilDetailResponse response = NungilDetailResponse.create(nungil, qaList,myAnsweredQa, getImageUrlList(nungil.getOpponent()));
        return response;
    }

    /**
     * 추천된 눈길 수동 삭제 api입니다.
     *
     * @param nungilId 눈길 id
     *
     */
    @Transactional
    public void deleteRecommendedNungil(Member member, Long nungilId){
        Nungil nungil = nungilRepository.findById(nungilId)
                .orElseThrow(() -> new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        if(!nungil.getMember().equals(member)){
            throw new GeneralException(NungilErrorResult.NUNGIL_WRONG_MEMBER);
        }
        if(!nungil.getStatus().equals(NungilStatus.RECOMMENDED)){
            throw new GeneralException(NungilErrorResult.NUNGIL_WRONG_STATUS);
        }
        nungilRepository.deleteById(nungilId);
    }

    /**
     * 만료된 눈길 자동 삭제 api입니다.
     */
    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredNungil(){
        LocalDateTime now = LocalDateTime.now();
        List<Nungil> expiredNungilList = nungilRepository.findByExpiredAtAfter(now);

        if (!expiredNungilList.isEmpty()) {
            nungilRepository.deleteAll(expiredNungilList);
        }
    }
    private List<String> getImageUrlList(Member member){
        List<String> imageUrlList = member.getMemberImageList().stream()
                .map(MemberImage::getFilename)
                .map(filename -> presignedUrlService.generatePresignedDownloadUrl(filename.toString()))
                .collect(Collectors.toList());
        return imageUrlList;
    }
}
