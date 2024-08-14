package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionCategory {
    LIFESTYLE("LIFESTYLE"),
    FINANCE("FINANCE"),
    HOUSEWORK("HOUSEWORK"),
    VISION("VISION"),
    FAMILY("FAMILY"),
    ;
    private final String title;
}
