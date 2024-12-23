package com.igloo_club.nungil_v3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyVerificationRequest {

    @Schema(description = "인증번호 6자리", example = "123456")
    private String code;

    @Schema(description = "회사 이메일", example = "soo@soongsil.ac.kr")
    private String email;
}
