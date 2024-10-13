package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.ChatRoom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomCreateResponse {

    private Long id;

    public static ChatRoomCreateResponse create(ChatRoom chatRoom) {
        ChatRoomCreateResponse response = new ChatRoomCreateResponse();

        response.id = chatRoom.getId();

        return response;
    }
}
