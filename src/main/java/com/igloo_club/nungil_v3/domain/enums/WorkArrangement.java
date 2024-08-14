package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkArrangement {
    ROTATIONAL("순환근무"),
    SHIFT("교대근무"),
    NONE("해당 사항 없음"),
    ;

    private final String title;
}
