package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.ImageStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ImageUploadNotifyRequest {

    @NotNull
    @Schema(description = "업로드한 파일명", example = "9c7f7afe-c95f-4c11-a025-233974c57c9a")
    private UUID filename;

    @NotNull
    @Schema(description = "업로드 상태", example = "UPLOAD_COMPLETE")
    private ImageStatus status;
}
