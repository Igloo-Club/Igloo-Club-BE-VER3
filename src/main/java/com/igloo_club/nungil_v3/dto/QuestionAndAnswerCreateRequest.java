package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.enums.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAndAnswerCreateRequest {
    @NotNull
    private Question question;

    @NotNull
    @Size(max = 255)
    private String answer;

    private Long exposureOrder;
}
