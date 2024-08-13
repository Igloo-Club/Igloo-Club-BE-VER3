package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.Question;
import com.igloo_club.nungil_v3.domain.enums.QuestionCategory;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerCreateRequest;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAndAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(value = EnumType.STRING)
    private Question question;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(value = EnumType.STRING)
    private QuestionCategory questionCategory;

    @Column(columnDefinition = "VARCHAR(255)")
    private String answer;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    @Builder.Default
    private Long exposureOrder = null;

    // == 정적 생성 메서드 == //
    public static QuestionAndAnswer create(QuestionAndAnswerCreateRequest request, Member member){
        return QuestionAndAnswer.builder()
                .member(member)
                .question(request.getQuestion())
                .questionCategory(request.getQuestion().getCategory())
                .answer(request.getAnswer())
                .exposureOrder(request.getExposureOrder())
                .build();
    }

    // == 비즈니스 메서드 == //
    public void updateOrderToNull(){
        this.exposureOrder = null;
    }

    public void update(QuestionAndAnswerUpdateRequest request){
        this.answer = request.getAnswer();
        this.exposureOrder = request.getExposureOrder();
    }
}
