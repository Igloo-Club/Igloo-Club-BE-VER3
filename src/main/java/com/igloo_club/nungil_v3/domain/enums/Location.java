package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Location {
    PANGYO("판교"),
    GWANGHWAMUN("광화문"),
    GANGNAM("강남"),
    YEOUIDO("여의도"),
    ;

    private final String title;
}
