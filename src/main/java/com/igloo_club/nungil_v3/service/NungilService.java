package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.*;
import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.NungilErrorResult;
import com.igloo_club.nungil_v3.mapper.NungilMapper;
import com.igloo_club.nungil_v3.repository.BlockedMemberRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import com.igloo_club.nungil_v3.repository.NungilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NungilService {
    private final MemberService memberService;

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
        if (recommendedMember == null) return null;

        // 4. 추천 받은 회원에 대한 차단(중복방지)를 생성하고 저장한다.
        BlockedMember blockedMember = getBlockedMember(member, recommendedMember);
        blockedMemberRepository.save(blockedMember);

        // 5. 추천 받은 회원에 대한 눈길을 생성하고 저장한다.
        Nungil newNungil = Nungil.create(member, recommendedMember, NungilStatus.RECOMMENDED);
        nungilRepository.save(newNungil);

        // 6. 추천이 정상적으로 동작했을 시 drawCount를 1 증가 시킨다.
        member.plusDrawCount();

        // 7. 추천 받은 회원 정보를 반환한다. -> mapper 수정하기
        return NungilMapper.INSTANCE.toResponse(newNungil);
        // + 시간 초과 시 drawCount 초기화
    }

    private Member getRecommendedMember(Member currentMember) {
        List<Member> membersList = memberRepository.findBySex(currentMember.getOppositeSex());
        List<BlockedMember> blockedMembersList = blockedMemberRepository.findByMember(currentMember);

        List<Long> blockedMemberIds = blockedMembersList.stream()
                .map(blockedMember -> blockedMember.getOpponent().getId())
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
                .filter(entry -> !blockedMemberIds.contains(entry.getKey().getId())) // Block된 멤버 제외
                .filter(entry ->
                        entry.getKey().getLocation().stream()
                                .anyMatch(location -> currentMember.getLocation().contains(location))) // location 비교
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
    private void resetDrawCountForAllMembers() {
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
}
