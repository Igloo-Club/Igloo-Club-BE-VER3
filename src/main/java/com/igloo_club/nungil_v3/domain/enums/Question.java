package com.igloo_club.nungil_v3.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Question {
    LIFESTYLE1("나는 아침형 인간 VS 저녁형 인간",QuestionCategory.LIFESTYLE, 1),
    LIFESTYLE2("주말에는 역시 돌아다니기 VS 집에 있기", QuestionCategory.LIFESTYLE, 2),
    LIFESTYLE3("나의 평일 일과를 알려주세요.", QuestionCategory.LIFESTYLE, 3),
    FINANCE1("결혼을 한다면 경제권은 누가 가져야 할까요?", QuestionCategory.FINANCE, 1),
    FINANCE2("자산은 어떻게 운영 중이실까요?", QuestionCategory.FINANCE, 2),
    FINANCE3("안정적인 결혼 가능하신가요?", QuestionCategory.FINANCE, 3),
    FINANCE4("한달에 얼마나 지출하고 저축하시나요?", QuestionCategory.FINANCE, 4),
    HOUSEWORK1("어떤 방식의 육아를 선호하시나요?", QuestionCategory.HOUSEWORK, 1),
    HOUSEWORK2("자녀계획 있으세요?", QuestionCategory.HOUSEWORK, 2),
    HOUSEWORK3("가사는 어떻게 분담 하는 걸 선호 하시나요?", QuestionCategory.HOUSEWORK, 3),
    VISION1("일과 가정이 양립할 수 없다면 나는?",  QuestionCategory.VISION, 1),
    VISION2("미래 계획이 어떻게 되세요? 10년 내 목표를 말씀해주세요 (결혼, 출산, 육아, 커리어 전부 상관 없어요)", QuestionCategory.VISION, 2),
    VISION3("무슨 일 하고 계세요?", QuestionCategory.VISION, 3),
    FAMILY1("부모님 노후준비가 돼있으실까요?", QuestionCategory.FAMILY, 1),
    FAMILY2("형제 분들에 대해 애기해주세요.", QuestionCategory.FAMILY, 2),
    FAMILY3("부모님이 개입이 심하신 편일까요?", QuestionCategory.FAMILY, 3),
    ;
    private final String title;
    private final QuestionCategory category;
    private final Integer sortNum;

}
