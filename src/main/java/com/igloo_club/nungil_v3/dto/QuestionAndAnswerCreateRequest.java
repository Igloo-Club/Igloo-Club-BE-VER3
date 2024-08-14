package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.QuestionAndAnswer;
import com.igloo_club.nungil_v3.domain.enums.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAndAnswerCreateRequest {
    @NotNull
    private Question question;

    @NotNull
    @Size(max = 255)
    private String answer;

    private Long exposureOrder;

    // == 정적 생성 메서드 == //
    public static QuestionAndAnswer create(QuestionAndAnswerCreateRequest request, Member member){
        return QuestionAndAnswer.builder()
                .member(member)
                .question(request.getQuestion())
                .questionCategory(request.getQuestion().getCategory())
                .answer(request.getAnswer())
                .exposureOrder(request.getExposureOrder())
                .build();
    }

    public void update(QuestionAndAnswerUpdateRequest request){
        this.answer = request.getAnswer();
        this.exposureOrder = request.getExposureOrder();
    }

}


