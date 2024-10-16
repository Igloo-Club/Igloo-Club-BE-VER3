package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuestionAndAnswerErrorResult implements ErrorResult{
    WRONG_ID(HttpStatus.BAD_REQUEST, "Question and answer id is not valid"),
    WRONG_QUESTION(HttpStatus.BAD_REQUEST, "Question is not answered or valid"),
    ANSWERED_QUESTION(HttpStatus.BAD_REQUEST, "Question is already answered"),
    SAME_CATEGORY_QUESTION_EXPOSING(HttpStatus.BAD_REQUEST, "There is exposing question which has same category"),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
