package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public enum FCMErrorResult implements ErrorResult{
    NO_FCM_TOKEN(HttpStatus.BAD_REQUEST, "There is no FCM Token on database"),
    FCM_TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "FCM Token is valid"),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
