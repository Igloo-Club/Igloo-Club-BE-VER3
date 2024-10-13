package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatMessageErrorResult implements ErrorResult {
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the chat message"),
    ONLY_MESSAGE_AUTHOR_CAN_DELETE(HttpStatus.FORBIDDEN, "You can only delete your messages you have written");

    private final HttpStatus httpStatus;
    private final String message;
}