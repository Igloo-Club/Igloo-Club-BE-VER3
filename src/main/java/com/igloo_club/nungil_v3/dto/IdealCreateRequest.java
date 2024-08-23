package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.Ideal;
import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.IdealErrorResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdealCreateRequest {
    private int preferredAgeStart;

    private int preferredAgeEnd;

    private int preferredHeightStart;

    private int preferredHeightEnd;

    private List<MbtiType> mbtiList = new ArrayList<>();

    private Boolean smoke;

    private Religion religion;

    private Integer marriagePlan;

    public Ideal toIdeal(){
        Ideal ideal = Ideal.builder()
                .preferredAgeStart(this.preferredAgeStart)
                .preferredAgeEnd(this.preferredAgeEnd)
                .preferredHeightStart(this.preferredHeightStart)
                .preferredHeightEnd(this.preferredHeightEnd)
                .smoke(this.smoke)
                .religion(this.religion)
                .marriagePlan(this.marriagePlan)
                .build();

        this.mbtiList.forEach(ideal::addMbtiType);

        if (preferredAgeStart > preferredAgeEnd){
             throw new GeneralException(IdealErrorResult.IDEAL_AGE_PARADOX);
        }

        if (preferredHeightStart > preferredHeightEnd){
            throw new GeneralException(IdealErrorResult.IDEAL_HEIGHT_PARADOX);
        }

        return ideal;
    }
}
