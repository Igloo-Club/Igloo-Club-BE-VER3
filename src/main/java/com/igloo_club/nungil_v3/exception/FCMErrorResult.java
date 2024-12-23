package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public enum FCMErrorResult implements ErrorResult{
    NO_FCM_TOKEN_IN_DB(HttpStatus.BAD_REQUEST, "There is no FCM Token on database"),
    FCM_TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "FCM Token is not valid"),
    FCM_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "Failed to find the FCM token in cookie"),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
