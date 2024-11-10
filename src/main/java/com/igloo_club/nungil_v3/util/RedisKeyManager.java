package com.igloo_club.nungil_v3.util;

public class RedisKeyManager {

    public static String getChatRoomMemberKey(Long chatRoomId, Long memberId) {
        return String.format("chatroom:%d:%d", chatRoomId, memberId);
    }
}
