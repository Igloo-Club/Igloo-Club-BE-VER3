package com.igloo_club.nungil_v3.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class MemberImageUploadUrlCreateResponse {

    private final String presignedUrl;

    private final UUID filename;

    public static MemberImageUploadUrlCreateResponse create(String presignedUrl, UUID filename) {
        return new MemberImageUploadUrlCreateResponse(presignedUrl, filename);
    }
}
