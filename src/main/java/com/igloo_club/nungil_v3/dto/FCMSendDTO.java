package com.igloo_club.nungil_v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class FCMSendDTO {
    private String token;

    private String title;

    private String body;


    public FCMSendDTO(String token, String title, String body){
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
