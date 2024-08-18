package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IdealErrorResult implements ErrorResult{
    IDEAL_AGE_PARADOX(HttpStatus.BAD_REQUEST, "Ideal age start is bigger than end"),
    IDEAL_HEIGHT_PARADOX(HttpStatus.BAD_REQUEST, "Ideal height start is bigger than end"),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
