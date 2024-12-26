package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.ChatMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private Long chatRoomId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private Long senderId;
    private ChatMessageStatus status;

    public static ChatDTO of(Long chatRoomId, Member member, ChatMessage chatMessage) {
        return new ChatDTO(chatRoomId,
                member.getNickname(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                member.getId(),
                chatMessage.getStatus());
    }

    public static ChatDTO of(Long chatRoomId, Member member, ChatMessageStatus status) {
        return new ChatDTO(chatRoomId,
                member.getNickname(),
                "",
                LocalDateTime.now(),
                member.getId(),
                status);
    }
}
