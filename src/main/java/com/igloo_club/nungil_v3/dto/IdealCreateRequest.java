package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.Ideal;
import com.igloo_club.nungil_v3.domain.enums.MbtiElem;
import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Preference;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.IdealErrorResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdealCreateRequest {
    private int preferredAgeStart;

    private int preferredAgeEnd;

    private int preferredHeightStart;

    private int preferredHeightEnd;

    private List<MbtiElem> mbtiElemList = new ArrayList<>();

    private Preference smoke;

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

        List<MbtiType> mbtiTypeList = convertToMbtiTypes(mbtiElemList);
        mbtiTypeList.forEach(ideal::addMbtiType);

        if (preferredAgeStart > preferredAgeEnd){
             throw new GeneralException(IdealErrorResult.IDEAL_AGE_PARADOX);
        }

        if (preferredHeightStart > preferredHeightEnd){
            throw new GeneralException(IdealErrorResult.IDEAL_HEIGHT_PARADOX);
        }

        return ideal;
    }

    public List<MbtiType> convertToMbtiTypes(List<MbtiElem> mbtiElemList) {
        return Stream.of(MbtiType.values())
                .filter(mbtiType -> matchesMbtiElemList(mbtiType, mbtiElemList))
                .collect(Collectors.toList());
    }

    private boolean matchesMbtiElemList(MbtiType mbtiType, List<MbtiElem> preferences) {
        // mbtiType의 이름이 preferences에 있는 요소로만 구성되어 있고, 길이도 동일한지 확인
        String typeString = mbtiType.name();

        // mbtiType의 각 문자가 preferences에 모두 포함되는지 확인
        return typeString.chars()
                .mapToObj(c -> (char) c)
                .allMatch(c -> preferences.contains(MbtiElem.valueOf(String.valueOf(c))));
    }
}
