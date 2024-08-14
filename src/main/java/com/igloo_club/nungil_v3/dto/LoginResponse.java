package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.RegisterProgress;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;

    // 다음 수행할 가입 절차
    private RegisterProgress nextProgress;


    private Boolean isProfileRegistered;
}
