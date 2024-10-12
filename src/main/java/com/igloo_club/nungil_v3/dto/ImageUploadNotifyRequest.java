package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.ImageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ImageUploadNotifyRequest {
    private UUID filename;

    private ImageStatus status;
}
