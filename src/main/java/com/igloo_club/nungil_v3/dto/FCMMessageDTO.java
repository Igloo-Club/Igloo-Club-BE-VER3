package com.igloo_club.nungil_v3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FCMMessageDTO {
    private boolean validateOnly;
    private FCMMessageDTO.Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private FCMMessageDTO.Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
