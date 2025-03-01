package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegisterProgress {
    COMPANY_EMAIL("회사 이메일 입력"),
    COMPANY_NAME("회사명 입력"),
    ESSENTIAL_PROFILE("필수 프로필 등록"),
    ADDITIONAL_PROFILE("상세 프로필 등록"),
    REGISTERED("가입완료"),
    IDEAL_REGISTERED("이상형 등록")
    ;

    private final String title;
}
