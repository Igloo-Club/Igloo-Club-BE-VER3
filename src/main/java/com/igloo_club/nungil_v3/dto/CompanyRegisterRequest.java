package com.igloo_club.nungil_v3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegisterRequest {
    private String email;
    private String companyName;
}
