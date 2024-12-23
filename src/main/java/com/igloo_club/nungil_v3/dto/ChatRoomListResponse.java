package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.ChatMessage;
import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomListResponse {

    private String nickname;

    private String content;

    private LocalDateTime createdAt;

    private String imageUrl;

    private Long chatRoomId;

    private int unreadCnt = 0;

    public static ChatRoomListResponse create(ChatRoom chatRoom, ChatMessage lastMessage, Member opponent, String imageUrl, int unreadCnt) {

        ChatRoomListResponse response = new ChatRoomListResponse();

        response.nickname = opponent.getNickname();
        response.content = "지금 연락을 시작하세요!";
        response.createdAt = chatRoom.getLastMessageAt();
        response.imageUrl = imageUrl;
        response.chatRoomId = chatRoom.getId();
        response.unreadCnt = unreadCnt;

        if (lastMessage != null) {
            response.content = lastMessage.getContent();
        }

        return response;
    }
}
