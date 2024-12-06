package com.igloo_club.nungil_v3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEmailRequest {

    @Schema(description = "회사 이메일", example = "soo@soongsil.ac.kr")
    private String email;
}
