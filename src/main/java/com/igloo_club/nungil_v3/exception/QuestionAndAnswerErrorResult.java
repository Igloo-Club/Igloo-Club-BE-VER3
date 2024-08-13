package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuestionAndAnswerErrorResult implements ErrorResult{
    WRONG_ID(HttpStatus.BAD_REQUEST, "Question and answer id is not valid"),
    WRONG_QUESTION(HttpStatus.BAD_REQUEST, "Question is not answered or valid"),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
