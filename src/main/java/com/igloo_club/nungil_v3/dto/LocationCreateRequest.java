package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LocationCreateRequest {

    @NotNull
    @Schema(description = "근무지", example = "PANGYO")
    private final Location location;
}
