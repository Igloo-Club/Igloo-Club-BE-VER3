package com.igloo_club.nungil_v3.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FCMSendDTO {
    private String token;

    private String title;

    private String body;

    @Builder(toBuilder = true)
    public FCMSendDTO(String token, String title, String body){
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
