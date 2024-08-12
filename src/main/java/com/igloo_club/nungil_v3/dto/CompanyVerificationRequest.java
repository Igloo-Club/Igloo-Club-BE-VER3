package com.igloo_club.nungil_v3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyVerificationRequest {

    private String code;

    private String email;
}
