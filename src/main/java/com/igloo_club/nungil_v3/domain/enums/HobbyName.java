package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HobbyName {

    LOCAL("국내", HobbyCategory.TRIP),
    ABROAD("해외", HobbyCategory.TRIP),
    SPORTS("스포츠", HobbyCategory.EXERCISE),
    FITNESS("헬스/크로스핏", HobbyCategory.EXERCISE),
    PILATES("필라테스/요가", HobbyCategory.EXERCISE),
    MARTIAL_ART("무술", HobbyCategory.EXERCISE),
    SWIMMING("수영", HobbyCategory.EXERCISE),
    HUMANITY("인문학", HobbyCategory.READING),
    PHILOSOPHY("철학", HobbyCategory.READING),
    ECONIMIC("경제", HobbyCategory.READING),
    HISTORY("역사", HobbyCategory.READING),
    IDOL("아이돌", HobbyCategory.MUSIC),
    INDIE("인디", HobbyCategory.MUSIC),
    HIPHOP("힙합", HobbyCategory.MUSIC),
    BAND("밴드", HobbyCategory.MUSIC),
    BALLAD("발라드", HobbyCategory.MUSIC),
    MEALS("식사", HobbyCategory.COOKING),
    BAKING("베이킹", HobbyCategory.COOKING),
    COFFEE("커피", HobbyCategory.COOKING),
    COCKTAILS("칵테일", HobbyCategory.COOKING),
    BOARD("보드게임", HobbyCategory.GAME),
    ONLINE("온라인 게임", HobbyCategory.GAME),
    PORTRAIT("인물사진", HobbyCategory.PHOTOGRAPHY),
    LANDSCAPE("풍경사진", HobbyCategory.PHOTOGRAPHY),
    HORROR("공포", HobbyCategory.MOVIE),
    ROMANCE("멜로", HobbyCategory.MOVIE),
    ACTION("액션", HobbyCategory.MOVIE),
    NOIR("느와르", HobbyCategory.MOVIE),
    COMEDY("코믹", HobbyCategory.MOVIE),
    ;

    private final String name;

    private final HobbyCategory category;
}
