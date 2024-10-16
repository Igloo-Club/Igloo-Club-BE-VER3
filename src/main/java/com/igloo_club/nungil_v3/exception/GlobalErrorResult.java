package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorResult implements ErrorResult {

    REDIS_NOT_FOUND(HttpStatus.BAD_REQUEST, "The requested data is not available or has expired"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    JSON_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Problem occurred when processing json"),
    MESSAGING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Problem occurered when sending email"),
    IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Problem occurred in I/O operation"),
    PRESIGNED_URL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate presigned url"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
