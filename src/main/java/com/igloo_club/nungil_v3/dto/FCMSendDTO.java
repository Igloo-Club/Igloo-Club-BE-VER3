package com.igloo_club.nungil_v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class FCMSendDTO {
    private String title;

    private String body;


    public FCMSendDTO(String title, String body){
        this.title = title;
        this.body = body;
    }
}
