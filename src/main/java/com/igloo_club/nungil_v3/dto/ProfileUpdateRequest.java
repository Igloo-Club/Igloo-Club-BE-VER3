package com.igloo_club.nungil_v3.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {
    @NotBlank
    @Size(max = 8)
    private String nickname;

    @NotNull
    private Sex sex;

    @NotNull
    @Past
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate birthdate;

    @NotNull
    private Location location;
}
