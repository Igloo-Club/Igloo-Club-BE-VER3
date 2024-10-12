package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.ChatMessage;
import com.igloo_club.nungil_v3.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageListResponse {

    private String nickname;

    private String content;

    private LocalDateTime createdAt;

    private Boolean isSender;

    public static ChatMessageListResponse create(Member member, ChatMessage chatMessage, Boolean isSender) {
        ChatMessageListResponse response = new ChatMessageListResponse();

        response.nickname = member.getNickname();
        response.content = chatMessage.getContent();
        response.createdAt = chatMessage.getCreatedAt();
        response.isSender = isSender;

        return response;
    }
}
