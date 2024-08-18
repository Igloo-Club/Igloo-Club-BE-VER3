package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.Profile;
import com.igloo_club.nungil_v3.domain.enums.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class AdditionalProfileCreateRequest {
    private final Integer height;

    private final Religion religion;

    private final Boolean tattoo;

    private final Boolean smoke;

    private final Integer marriagePlan;

    private final MbtiType mbtiType;

    private final Integer grossSalary;

    private final String job;

    private final List<WorkArrangement> workArrangementList = new ArrayList<>();

    private final CompanyScale scale;

    private final List<HobbyRequest> hobbyList = new ArrayList<>();

    private final String intro;

    public Profile toProfile() {
        Profile profile = Profile.builder()
                .height(this.height)
                .religion(this.religion)
                .tattoo(this.tattoo)
                .smoke(this.smoke)
                .marriagePlan(this.marriagePlan)
                .mbtiType(this.mbtiType)
                .grossSalary(this.grossSalary)
                .job(this.job)
                .intro(this.intro)
                .build();

        this.workArrangementList.forEach(profile::addWorkArrangement);
        this.hobbyList.forEach(hobbyRequest -> profile.addHobby(hobbyRequest.getCategory(), hobbyRequest.getName()));

        return profile;
    }

    @Getter
    @NoArgsConstructor
    private static class HobbyRequest {
        private HobbyCategory category;
        private HobbyName name;
    }
}
