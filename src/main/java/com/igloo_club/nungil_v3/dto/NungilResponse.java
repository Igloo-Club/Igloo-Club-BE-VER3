package com.igloo_club.nungil_v3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.igloo_club.nungil_v3.domain.*;
import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import com.igloo_club.nungil_v3.service.PresignedUrlService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NungilResponse {
    private Long nungilId;

    private List<String> imageUrlList;

    private String nickname;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate birthdate;

    private String companyName;

    private Integer height;

    private Religion religion;

    private Boolean tattoo;

    private Boolean smoke;

    private Integer marriagePlan;

    private MbtiType mbtiType;

    public static NungilResponse create(Nungil nungil, PresignedUrlService presignedUrlService){
        Member opponent = nungil.getOpponent();
        Company opponentCompany = opponent.getCompany();
        Profile opponentProfile = opponent.getProfile();

        List<String> imageUrlList = opponent.getMemberImageList().stream()
                .map(MemberImage::getFilename)
                .map(filename -> presignedUrlService.generatePresignedDownloadUrl(filename.toString()))
                .collect(Collectors.toList());

        NungilResponse response = new NungilResponse();

        response.nungilId = nungil.getId();
        response.nickname = opponent.getNickname();
        response.birthdate = opponent.getBirthdate();
        response.companyName = opponentCompany.getCompanyName();
        response.height = opponentProfile.getHeight();
        response.religion = opponentProfile.getReligion();
        response.tattoo = opponentProfile.getTattoo();
        response.smoke = opponentProfile.getSmoke();
        response.marriagePlan = opponentProfile.getMarriagePlan();
        response.mbtiType = opponentProfile.getMbtiType();
        response.imageUrlList = imageUrlList;

        return response;
    }

}
