package com.igloo_club.nungil_v3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorResult implements ErrorResult {
    CHAT_ROOM_MORE_THAN_ONE(HttpStatus.CONFLICT, "More than one chat room"),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the chat room"),
    NOT_MEMBER(HttpStatus.FORBIDDEN, "Only available for the member of the chat room"),
    INACTIVE_CHATROOM(HttpStatus.BAD_REQUEST, "Inactive chat room"),
    MEMBER_CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the member-chatroom"),
    CHATROOM_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "Given chat room is already deleted by member"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}