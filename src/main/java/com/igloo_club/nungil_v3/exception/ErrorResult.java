package com.igloo_club.nungil_v3.exception;

import org.springframework.http.HttpStatus;

public interface ErrorResult {
    HttpStatus getHttpStatus();

    String getMessage();

    String name();
}
