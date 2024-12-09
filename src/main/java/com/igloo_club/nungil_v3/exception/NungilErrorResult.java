package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NungilErrorResult implements ErrorResult{
    LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, "Member exceeded the limit"),
    OUT_OF_TIME(HttpStatus.BAD_REQUEST, "Not appropriate time for free recommendation."),
    NO_RECOMMENDATION(HttpStatus.NO_CONTENT, "No one has been recommended."),
    NUNGIL_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the Nungil"),
    NUNGIL_WRONG_STATUS(HttpStatus.BAD_REQUEST, "Nungil's status is not correct"),
    NUNGIL_ALREADY_SENT(HttpStatus.BAD_REQUEST, "Nungil is already sent"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
