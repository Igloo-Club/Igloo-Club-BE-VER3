package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IdealResponse {
    private Integer preferredAgeStart;

    private Integer preferredAgeEnd;

    private Integer preferredHeightStart;

    private Integer preferredHeightEnd;

    private List<MbtiType> mbtiList;

    private Boolean smoke;

    private Religion religion;

    private Integer marriagePlan;

    // == 생성 메서드 == //
    public static IdealResponse create(
            Integer preferredAgeStart,
            Integer preferredAgeEnd,
            Integer preferredHeightStart,
            Integer preferredHeightEnd,
            List<MbtiType> mbtiList,
            Boolean smoke,
            Religion religion,
            Integer marriagePlan
            ) {
        return new IdealResponse(
                preferredAgeStart,
                preferredAgeEnd,
                preferredHeightStart,
                preferredHeightEnd,
                mbtiList,
                smoke,
                religion,
                marriagePlan);
    }
}
