package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.HobbyCategory;
import com.igloo_club.nungil_v3.domain.enums.HobbyName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HobbyRequest {

    @Schema(description = "큰 카테고리", example = "EXERCISE")
    private HobbyCategory category;

    @Schema(description = "작은 카테고리", example = "SWIMMING")
    private HobbyName name;
}
