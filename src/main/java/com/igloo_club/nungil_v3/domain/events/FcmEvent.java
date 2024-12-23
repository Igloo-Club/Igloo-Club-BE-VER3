package com.igloo_club.nungil_v3.domain.events;

import com.igloo_club.nungil_v3.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Validated
@RequiredArgsConstructor
public class FcmEvent {

    @NotNull
    private final String title;

    @NotNull
    private final String body;

    @NotNull
    private final Member member;
}
