package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorResult implements ErrorResult{
    ANONYMOUS_USER(HttpStatus.UNAUTHORIZED, "Anonymous user"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the User"),
    SEXLESS_USER(HttpStatus.UNPROCESSABLE_ENTITY, "User have no sex"),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "Given email is already in use"),
    ALREADY_USING(HttpStatus.BAD_REQUEST, "You are already using this email");

    private final HttpStatus httpStatus;
    private final String message;
}
