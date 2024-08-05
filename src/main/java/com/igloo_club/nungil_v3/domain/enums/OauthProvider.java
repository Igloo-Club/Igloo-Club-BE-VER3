package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthProvider {
    KAKAO("카카오"),
    ;

    private final String title;
}
