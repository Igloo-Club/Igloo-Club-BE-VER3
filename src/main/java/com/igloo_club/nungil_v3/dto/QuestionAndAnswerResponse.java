package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.QuestionAndAnswer;
import com.igloo_club.nungil_v3.domain.enums.Question;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionAndAnswerResponse {
    private Long qaId;

    private Question question;

    private String questionTitle;

    private String answer;

    private Long exposureOrder;

    public static QuestionAndAnswerResponse create(QuestionAndAnswer qa){
        QuestionAndAnswerResponse response = new QuestionAndAnswerResponse();
        response.qaId = qa.getId();
        response.question = qa.getQuestion();
        response.questionTitle = qa.getQuestion().getTitle();
        response.answer = qa.getAnswer();
        response.exposureOrder = qa.getExposureOrder();

        return response;
    }
}
