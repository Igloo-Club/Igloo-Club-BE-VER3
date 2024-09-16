package com.igloo_club.nungil_v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseWrapper<T> {
    @JsonProperty
    private T result;           // 결과 데이터

    @JsonProperty
    private int resultCode;      // 결과 코드 (ex. 200, 400, 500 등)

    @JsonProperty
    private String resultMsg;    // 결과 메시지 (성공/실패 메시지)
}
