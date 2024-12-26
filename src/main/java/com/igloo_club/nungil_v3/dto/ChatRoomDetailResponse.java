package com.igloo_club.nungil_v3.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDetailResponse {


    private String nickname;

    private String imageUrl;

    private Long chatRoomId;

    // WebSocket으로 발행된 메시지가 어느 사용자의 것인지 구분하기 위해 사용
    private Long ownMemberId;

    private Boolean isInactive;

    private Slice<ChatMessageResponse> messageSlice;


    public static ChatRoomDetailResponse create(String nickname, String imageUrl, Long chatRoomId, Long ownMemberId, Boolean isInactive, Slice<ChatMessageResponse> messageSlice) {
        ChatRoomDetailResponse response = new ChatRoomDetailResponse();

        response.nickname = nickname;
        response.imageUrl = imageUrl;
        response.chatRoomId = chatRoomId;
        response.ownMemberId = ownMemberId;
        response.isInactive = isInactive;
        response.messageSlice = messageSlice;

        return response;
    }
}
