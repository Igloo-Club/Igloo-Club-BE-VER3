package com.igloo_club.nungil_v3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.igloo_club.nungil_v3.domain.*;
import com.igloo_club.nungil_v3.domain.enums.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileResponse {

    private String nickname;

    private Sex sex;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate birthdate;

    private Integer height;

    private Religion religion;

    private Boolean tattoo;

    private Boolean smoke;

    private Integer marriagePlan;

    private MbtiType mbti;

    private Integer grossSalary;

    private String job;

    private List<WorkArrangement> workArrangementList;

    private CompanyScale scale;

    private List<HobbyResponse> hobbyList;

    private String intro;

    private List<String> imageUrlList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class HobbyResponse {
        private HobbyCategory category;
        private HobbyName name;
    }

    public static MemberProfileResponse create(Member member, Profile profile, List<String> imageUrlList) {
        MemberProfileResponse response = new MemberProfileResponse();

        response.nickname = member.getNickname();
        response.sex = member.getSex();
        response.birthdate = member.getBirthdate();
        response.job = member.getJob();

        if (profile != null) {
            response.height = profile.getHeight();
            response.religion = profile.getReligion();
            response.tattoo = profile.getTattoo();
            response.smoke = profile.getSmoke();
            response.marriagePlan = profile.getMarriagePlan();
            response.mbti = profile.getMbtiType();
            response.grossSalary = profile.getGrossSalary();
            response.workArrangementList = profile.getWorkArrangementList();
            response.hobbyList = profile.getHobbyList().stream()
                    .map(hobby -> new HobbyResponse(hobby.getCategory(), hobby.getName()))
                    .collect(Collectors.toList());
            response.intro = profile.getIntro();
        }

        Company company = member.getCompany();
        if (company != null) {
            response.scale = company.getScale();
        }

        response.imageUrlList = imageUrlList;

        return response;
    }
}
