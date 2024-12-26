package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.Ideal;
import com.igloo_club.nungil_v3.domain.enums.MbtiElem;
import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IdealResponse {
    private Integer preferredAgeStart;

    private Integer preferredAgeEnd;

    private Integer preferredHeightStart;

    private Integer preferredHeightEnd;

    private List<MbtiElem> mbtiElemList;

    private Boolean smoke;

    private Religion religion;

    private Integer marriagePlan;

    // == 생성 메서드 == //
    public static IdealResponse create(Ideal ideal) {
        return  new IdealResponse(
                ideal.getPreferredAgeStart(),
                ideal.getPreferredAgeEnd(),
                ideal.getPreferredHeightStart(),
                ideal.getPreferredHeightEnd(),
                convertToMbtiElems(ideal.getMbtiList()),
                ideal.getSmoke(),
                ideal.getReligion(),
                ideal.getMarriagePlan());
    }

    private static List<MbtiElem> convertToMbtiElems(List<MbtiType> mbtiTypeList) {
        return mbtiTypeList.stream()
                // 각 MbtiType을 개별 문자로 나누어 MbtiElem으로 변환
                .flatMap(mbtiType -> mbtiType.name().chars()
                        .mapToObj(c -> MbtiElem.valueOf(String.valueOf((char) c))))
                // 중복 제거
                .distinct()
                .collect(Collectors.toList());
    }

}
