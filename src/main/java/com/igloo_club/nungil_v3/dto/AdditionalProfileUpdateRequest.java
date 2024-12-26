package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class AdditionalProfileUpdateRequest {

    @Schema(description = "키", example = "180")
    private final Integer height;

    @Schema(description = "종교", example = "BUDDHISM")
    private final Religion religion;

    @Schema(description = "문신 여부", example = "true")
    private final Boolean tattoo;

    @Schema(description = "흡연 여부", example = "false")
    private final Boolean smoke;

    @Schema(description = "결혼 계획", example = "7")
    private final Integer marriagePlan;

    @Schema(description = "MBTI", example = "ISFP")
    private final MbtiType mbtiType;

    @Schema(description = "세전 연봉", example = "4")
    private final Integer grossSalary;

    @Schema(description = "취미 목록", implementation = HobbyRequest.class,
            example = "[{\"category\": \"EXERCISE\", \"name\": \"SWIMMING\" }," +
                    "{\"category\": \"MUSIC\", \"name\": \"INDIE\" }," +
                    "{\"category\": \"MUSIC\", \"name\": \"BAND\" }]")
    private final List<HobbyRequest> hobbyList = new ArrayList<>();

    @Schema(description = "한 줄 자기소개", example = "수영을 좋아하는 수영입니다")
    private final String intro;
}
