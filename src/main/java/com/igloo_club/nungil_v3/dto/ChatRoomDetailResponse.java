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

    private Slice<ChatMessageResponse> messageSlice;


    public static ChatRoomDetailResponse create(String nickname, String imageUrl, Long chatRoomId, Slice<ChatMessageResponse> messageSlice) {
        ChatRoomDetailResponse response = new ChatRoomDetailResponse();

        response.nickname = nickname;
        response.imageUrl = imageUrl;
        response.chatRoomId = chatRoomId;
        response.messageSlice = messageSlice;

        return response;
    }
}
