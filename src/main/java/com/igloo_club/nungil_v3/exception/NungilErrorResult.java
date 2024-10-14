package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NungilErrorResult implements ErrorResult{
    LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, "You have exceeded the limit"),
    OUT_OF_TIME(HttpStatus.BAD_REQUEST, "Not appropriate time for free recommendation."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
