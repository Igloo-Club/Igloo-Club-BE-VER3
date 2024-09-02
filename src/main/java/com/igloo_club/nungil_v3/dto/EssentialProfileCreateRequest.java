package com.igloo_club.nungil_v3.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EssentialProfileCreateRequest {
    @NotBlank
    @Size(max = 8)
    @Schema(description = "닉네임", example = "하늘소")
    private String nickname;

    @NotNull
    @Schema(description = "성별", example = "MALE")
    private Sex sex;

    @NotNull
    @Past
    @JsonFormat(pattern = "yyyyMMdd")
    @Schema(description = "생년월일", example = "20001026")
    private LocalDate birthdate;
}
