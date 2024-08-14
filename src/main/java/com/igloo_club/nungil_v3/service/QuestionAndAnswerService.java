package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.QuestionAndAnswer;
import com.igloo_club.nungil_v3.domain.enums.Question;
import com.igloo_club.nungil_v3.domain.enums.QuestionCategory;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerCreateRequest;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerResponse;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerUpdateRequest;
import com.igloo_club.nungil_v3.dto.QuestionListResponse;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.QuestionAndAnswerErrorResult;
import com.igloo_club.nungil_v3.repository.QuestionAndAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionAndAnswerService {
    private final QuestionAndAnswerRepository questionAndAnswerRepository;

    /**
     * 신규 질의응답을 생성하는 메소드입니다
     * @param request 질의응답 생성 요청 DTO
     * @param member 질의응답 생성하는 회원
     */
    @Transactional
    public void createQuestionAndAnswer(QuestionAndAnswerCreateRequest request, Member member){
        // 신규 질의 응답 객체 생성
        QuestionAndAnswer qa = QuestionAndAnswer.create(request, member);
        Long order = request.getExposureOrder();

        // DB를 조회해 기존 질의응답 객체 존재할 시 예외 처리
        if(questionAndAnswerRepository.findQuestionAndAnswerByMemberAndQuestion(member, request.getQuestion()).isPresent()){
            throw  new GeneralException(QuestionAndAnswerErrorResult.ANSWERED_QUESTION);
        }

        // 신규 질의 응답과 노출 순서 중복 여부 확인
        // 중복 있을 시 기존 질의 응답 노출 순서 null로 변경
        handleExposureOrderConflict(member, order);

        // 신규 질의 응답 저장
        questionAndAnswerRepository.save(qa);
    }

    /**
     * 주어진 질의응답의 정보를 수정하는 메소드입니다
     * @param member 질의응답을 수정할 멤버
     * @param qaId 등록된 질의응답 id
     * @param request 질의응답 수정 요청 DTO
     */
    @Transactional
    public void updateQuestionAndAnswer(Long qaId, QuestionAndAnswerUpdateRequest request, Member member){
        QuestionAndAnswer qa = questionAndAnswerRepository.findQuestionAndAnswerById(qaId)
                        .orElseThrow(()-> new GeneralException(QuestionAndAnswerErrorResult.WRONG_ID));

        // 신규 질의 응답과 노출 순서 중복 여부 확인
        // 중복 있을 시 기존 질의 응답 노출 순서 null로 변경
        handleExposureOrderConflict(member, request.getExposureOrder());

        qa.update(request);
    }

    /**
     * 노출 순서 중복을 처리하는 메소드입니다
     * 중복이 있을 경우 기존 질의응답의 노출 순서를 null로 업데이트합니다
     *
     * @param member 질의응답을 생성하는 회원
     * @param order 노출 순서
     */
    private void handleExposureOrderConflict(Member member, Long order) {
        if (order != null) {
            Optional<QuestionAndAnswer> existingQa = questionAndAnswerRepository.findByMemberAndExposureOrder(member, order);

            existingQa.ifPresent(originalQa -> originalQa.updateOrderToNull());
        }
    }

    /**
     * 작성된 단일 질의응답을 조회하는 메소드입니다.
     *
     * @param member 질의응답을 조회할 멤버
     * @param question 질문
     *
     * @return QuestionAndAnswerResponse 객체
     */
    public QuestionAndAnswerResponse getQuestionAndAnswerResponseByMemberAndQuestion(Member member, Question question){
        QuestionAndAnswer qa = questionAndAnswerRepository.findQuestionAndAnswerByMemberAndQuestion(member, question)
                .orElseThrow(()->new GeneralException(QuestionAndAnswerErrorResult.WRONG_QUESTION));

        return QuestionAndAnswerResponse.create(qa);
    }


    /**
     * 특정 멤버의 전시 중인 질의응답들을 Slice로 조회하는 메소드입니다
     *
     * @param member 질의응답을 조회할 멤버
     * @param page 가져올 페이지 번호
     * @param size 페이지당 요소 수
     *
     * @return QuestionAndAnswerResponse의 페이지 객체
     */
    public Slice<QuestionAndAnswerResponse> getExposingQuestionAndAnswerPageByMember(Member member, int page, int size) {

        // pageRequest를 exposureOrder 순으로 생성
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("exposureOrder").ascending());

        // 특정 멤버와 노출 순서 범위로 QuestionAndAnswer 엔티티를 데이터베이스에서 조회
        Slice<QuestionAndAnswer> questionAndAnswerPage = questionAndAnswerRepository
                .findAllByMemberAndExposureOrderBetween(member, 0L, (long)size -1);

        // QuestionAndAnswer 엔티티를 QuestionAndAnswer DTO로 변환
        List<QuestionAndAnswerResponse> questionAndAnswerResponse = questionAndAnswerPage.getContent().stream()
                .map(QuestionAndAnswerResponse::create)
                .collect(Collectors.toList());

        // 다음 페이지 존재 여부 확인
        boolean hasNext = questionAndAnswerPage.hasNext();

        // 변환된 DTO 리스트와 함께 새로운 Slice 객체를 생성하여 반환
        return new SliceImpl<>(questionAndAnswerResponse, pageRequest, hasNext);
    }

    /**
     * 특정 카테고리의 질문들을 Slice로 답변 여부와 함께 조회하는 메소드입니다
     *
     * @param member 질문을 조회할 멤버
     * @param page 가져올 페이지 번호
     * @param size 페이지당 요소 수
     *
     * @return QuestionListResponse 슬라이스 객체
     */
    public Slice<QuestionListResponse> getQuestionList(Member member, QuestionCategory category, int page, int size){
        // pageRequest 생성
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("sortNum").ascending());

        // 특정 멤버와 카테고리에 해당하는 질의응답을 조회
        List<Question> answeredQuestion = questionAndAnswerRepository.findAllByMemberAndQuestionCategory(member, category).stream()
                .map(QuestionAndAnswer::getQuestion)
                .collect(Collectors.toList());

        // QuestionListResponse로 변환
        List<QuestionListResponse> questionListResponses = Arrays.stream(Question.values())
                .map(q-> QuestionListResponse.create(q, answeredQuestion))
                .collect(Collectors.toList());

        // 현재 페이지의 시작 인덱스 계산
        int start = Math.toIntExact(pageRequest.getOffset());
        int end = Math.min((start + pageRequest.getPageSize()), questionListResponses.size());

        // 현재 페이지에 해당하는 리스트 추출
        List<QuestionListResponse> currentSlice = questionListResponses.subList(start, end);

        // 다음 페이지가 있는지 여부를 판단
        boolean hasNext = end < questionListResponses.size();

        return new SliceImpl<>(currentSlice, pageRequest, hasNext);
    }



}
