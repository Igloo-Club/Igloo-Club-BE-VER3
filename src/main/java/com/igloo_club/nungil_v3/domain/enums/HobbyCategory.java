package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HobbyCategory {
    TRIP("여행"),
    EXERCISE("운동"),
    READING("독서"),
    MUSIC("음악"),
    COOKING("요리"),
    GAME("게임"),
    PHOTOGRAPHY("사진"),
    MOVIE("영화"),
    PET("반려동물"),
    ;

    private final String title;
}
