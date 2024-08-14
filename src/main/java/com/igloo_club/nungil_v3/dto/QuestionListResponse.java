package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.Question;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionListResponse {

    private Question question;

    private String questionTitle;

    private Boolean answered;

    public static QuestionListResponse create(Question question, List<Question> answeredQuestion){
        QuestionListResponse response = new QuestionListResponse();
        response.question = question;
        response.questionTitle = question.getTitle();
        response.answered = answeredQuestion.contains(question);

        return response;
    }
}
