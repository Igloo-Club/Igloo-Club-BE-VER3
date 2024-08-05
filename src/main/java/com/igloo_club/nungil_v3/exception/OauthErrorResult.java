package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OauthErrorResult implements ErrorResult{
    UNEXPECTED_OAUTH_ID(HttpStatus.BAD_REQUEST, "Given oauth is not valid"),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
