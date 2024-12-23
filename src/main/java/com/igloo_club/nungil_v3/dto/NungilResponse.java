package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NungilResponse {
    private Long id;

    private String nickname;

    private LocalDate birthdate;

    private String companyName;

    private Integer height;

    private Religion religion;

    private Boolean tattoo;

    private Boolean smoke;

    private Integer marriagePlan;

    private MbtiType mbtiType;

}
