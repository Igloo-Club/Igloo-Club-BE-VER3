package com.igloo_club.nungil_v3.exception;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{
    private final ErrorResult errorResult;

    public GeneralException(ErrorResult errorResult) {
        super(errorResult.getMessage());
        this.errorResult = errorResult;
    }

    public GeneralException(ErrorResult errorResult, Throwable cause) {
        super(errorResult.getMessage(), cause);
        this.errorResult = errorResult;
    }
}
