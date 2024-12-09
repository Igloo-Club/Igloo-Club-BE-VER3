package com.igloo_club.nungil_v3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.igloo_club.nungil_v3.domain.*;
import com.igloo_club.nungil_v3.domain.enums.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NungilDetailResponse {
    private Long nungilId;

    private NungilStatus status;

    private List<String> imageUrlList;

    private String nickname;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate birthdate;

    private String intro;

    // 회사 정보
    private String companyName;

    private String job;

    private CompanyScale scale;

    // 위치 정보
    private List<Location> locationList;

    // 인적 사항
    private Integer height;

    private Religion religion;

    private Boolean tattoo;

    private Boolean smoke;

    private Integer marriagePlan;

    private MbtiType mbtiType;

    private List<Hobby> hobbyList;

    // 1문 1답
    private Integer myAnsweredQa;
    private List<QuestionAndAnswerResponse> questionAndAnswerList;

    public static NungilDetailResponse create(Nungil nungil, List<QuestionAndAnswerResponse> qaList, int myAnsweredQa,List<String> imageUrlList){
        Member opponent = nungil.getOpponent();
        Company opponentCompany = opponent.getCompany();
        Profile opponentProfile = opponent.getProfile();

        NungilDetailResponse response = new NungilDetailResponse();
        response.nungilId = nungil.getId();
        response.status = nungil.getStatus();
        response.imageUrlList = imageUrlList;
        response.nickname = opponent.getNickname();
        response.birthdate = opponent.getBirthdate();
        response.intro = opponentProfile.getIntro();
        response.companyName = opponentCompany.getCompanyName();
        response.job = opponent.getJob();
        response.scale = opponentCompany.getScale();
        response.locationList = opponent.getLocation();
        response.height = opponentProfile.getHeight();
        response.religion = opponentProfile.getReligion();
        response.tattoo = opponentProfile.getTattoo();
        response.smoke = opponentProfile.getSmoke();
        response.marriagePlan = opponentProfile.getMarriagePlan();
        response.mbtiType = opponentProfile.getMbtiType();
        response.hobbyList = opponentProfile.getHobbyList();
        response.questionAndAnswerList = qaList;
        response.myAnsweredQa = myAnsweredQa;

        return response;
    }
}
