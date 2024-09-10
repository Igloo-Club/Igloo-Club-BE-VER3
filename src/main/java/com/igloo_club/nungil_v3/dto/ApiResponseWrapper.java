package com.igloo_club.nungil_v3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseWrapper<T> {
    private T result;           // 결과 데이터
    private int resultCode;      // 결과 코드 (ex. 200, 400, 500 등)
    private String resultMsg;    // 결과 메시지 (성공/실패 메시지)
}
