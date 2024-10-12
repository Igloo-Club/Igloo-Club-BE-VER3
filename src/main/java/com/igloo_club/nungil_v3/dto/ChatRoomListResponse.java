package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.ChatMessage;
import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomListResponse {

    private String nickname;

    private String content;

    private LocalDateTime createdAt;

    private Long chatRoomId;

    public static ChatRoomListResponse create(ChatRoom chatRoom, ChatMessage lastMessage, Member opponent) {

        ChatRoomListResponse response = new ChatRoomListResponse();

        response.nickname = opponent.getNickname();
        response.content = "지금 연락을 시작하세요!";
        response.createdAt = chatRoom.getLastMessageAt();
        response.chatRoomId = chatRoom.getId();

        if (lastMessage != null) {
            response.content = lastMessage.getContent();
        }

        return response;
    }
}
