package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.Question;
import com.igloo_club.nungil_v3.domain.enums.QuestionCategory;
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


    // == 비즈니스 메서드 == //
    public void updateOrderToNull(){
        this.exposureOrder = null;
    }

    public void update(String answer, Long exposureOrder){
        this.answer = answer;
        this.exposureOrder = exposureOrder;
    }
}
