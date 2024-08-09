package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyScale {
    LARGE("대기업"),
    MID("중견기업"),
    SMALL("중소기업"),
    STARTUP("스타트업"),
    ;

    private final String title;
}
