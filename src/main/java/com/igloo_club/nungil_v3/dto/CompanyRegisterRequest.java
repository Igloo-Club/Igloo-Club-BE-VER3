package com.igloo_club.nungil_v3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegisterRequest {

    @Schema(description = "회사 이메일", example = "soo@soongsil.ac.kr")
    private String email;

    @Schema(description = "회사명", example = "숭실대학교")
    private String companyName;
}
