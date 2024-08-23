package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Question {
    LIFESTYLE1("아침 형과 저녁 형중에 어떤 편이신가요?", null,QuestionCategory.LIFESTYLE, 1),
    LIFESTYLE2("주말에는 집에 있는걸 즐기는 편이신가요?", null, QuestionCategory.LIFESTYLE, 2),
    LIFESTYLE3("나의 평일 일과를 공유해주세요.",null, QuestionCategory.LIFESTYLE, 3),
    FINANCE1("결혼을 한다면 경제권은 누가 가져야 할까요?","나, 상대방, 각자 중에서 누가 갖는게 맞다고 생각하세요?", QuestionCategory.FINANCE, 1),
    FINANCE2("안정적인 결혼 가능하신가요?","결혼 계획에 대해 말씀해주세요.", QuestionCategory.FINANCE, 2),
    FINANCE3("자산은 어떻게 운영 중이실까요?","자산 포트폴리오나 투자 철학 등을 애기해주시면 좋아요", QuestionCategory.FINANCE, 3),
    FINANCE4("한달에 얼마나 지출하고 저축하시나요?", "%로 대답해주셔도 돼요! 소비 습관과 저축 습관을 애기해주세요.",QuestionCategory.FINANCE, 4),
    HOUSEWORK1("어떤 방식의 육아를 선호하시나요?", "본인의 육아 스타일에 대해 설명해주세요. 아이를 어떻게 키우고 싶은지 등을 애기해주시면 좋아요.",QuestionCategory.HOUSEWORK, 1),
    HOUSEWORK2("자녀계획 있으세요?", "언제 자녀를 가지고 싶으신가요?", QuestionCategory.HOUSEWORK, 2),
    HOUSEWORK3("가사는 어떻게 분담 하는 걸 선호 하시나요?", "상대방이 가사활동을 몇 % 정도 해주길 선호하시나요?", QuestionCategory.HOUSEWORK, 3),
    VISION1("일과 가정 중에 어디에 더 비중을 두고 싶으신가요?", null,  QuestionCategory.VISION, 1),
    VISION2("미래 계획이 어떻게 되세요? 10년 내 목표를 말씀해주세요 (결혼, 출산, 육아, 커리어 전부 상관 없어요)", null, QuestionCategory.VISION, 2),
    VISION3("무슨 일 하고 계세요?", "어느정도로 돼있는지 말씀해주세요.", QuestionCategory.VISION, 3),
    FAMILY1("부모님 노후준비가 돼있으실까요?", "어느정도로 돼있는지 말씀해주세요.", QuestionCategory.FAMILY, 1),
    FAMILY2("형제 분들에 대해 애기해주세요.", null, QuestionCategory.FAMILY, 2),
    FAMILY3("부모님이 개입이 심하신 편일까요?", null, QuestionCategory.FAMILY, 3),
    ;
    private final String title;
    private final String subTitle;
    private final QuestionCategory category;
    private final Integer sortNum;

}
