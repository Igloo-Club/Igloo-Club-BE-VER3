package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegisterProgress {
    COMPANY_EMAIL_INPUT("회사이메일입력"),
    COMPANY_EMAIL_VERIFICATION("회사이메일인증"),
    GENDER_BIRTHDATE_INPUT("성별생년월일입력"),
    NICKNAME_INPUT("닉네임입력"),
    REGISTERED("가입완료"),
    ;

    private final String title;
}
