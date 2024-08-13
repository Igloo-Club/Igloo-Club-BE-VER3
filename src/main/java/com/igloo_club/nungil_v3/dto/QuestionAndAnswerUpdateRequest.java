package com.igloo_club.nungil_v3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAndAnswerUpdateRequest {
    @NotNull
    @Size(max = 255)
    private String answer;

    private Long exposureOrder;
}
