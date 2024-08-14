package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Religion {
    BUDDHISM("불교"),
    CHRISTIANITY("기독교"),
    CATHOLICISM("천주교"),
    ISLAM("이슬람교"),
    OTHER("기타"),
    NONE("무교"),
    ;

    private final String title;
}
