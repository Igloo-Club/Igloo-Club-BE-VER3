package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LocationCreateRequest {

    @NotNull
    private final Location location;
}
