package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.Ideal;
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
    public static IdealResponse create(Ideal ideal) {
        return new IdealResponse(
                ideal.getPreferredAgeStart(),
                ideal.getPreferredAgeEnd(),
                ideal.getPreferredHeightStart(),
                ideal.getPreferredHeightEnd(),
                ideal.getMbtiList(),
                ideal.getSmoke(),
                ideal.getReligion(),
                ideal.getMarriagePlan());
    }
}
