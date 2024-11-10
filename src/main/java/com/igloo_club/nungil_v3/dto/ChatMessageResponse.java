package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.ChatMessage;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.ChatMessageStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageResponse {

    private Long id;

    private String nickname;

    private String content;

    private LocalDateTime createdAt;

    private Boolean isAuthor;

    private ChatMessageStatus status;

    public static ChatMessageResponse create(Member member, ChatMessage chatMessage, Boolean isAuthor) {
        ChatMessageResponse response = new ChatMessageResponse();

        response.id = chatMessage.getId();
        response.nickname = member.getNickname();
        response.content = chatMessage.getContent();
        response.createdAt = chatMessage.getCreatedAt();
        response.isAuthor = isAuthor;
        response.status = chatMessage.getStatus();

        return response;
    }
}
