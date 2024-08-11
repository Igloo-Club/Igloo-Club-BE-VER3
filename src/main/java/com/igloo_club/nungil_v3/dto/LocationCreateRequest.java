package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LocationCreateRequest {

    private final Location location;
}
