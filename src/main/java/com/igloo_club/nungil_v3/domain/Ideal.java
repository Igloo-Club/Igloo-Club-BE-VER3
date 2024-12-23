package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ideal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer preferredAgeStart;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer preferredAgeEnd;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer preferredHeightStart;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer preferredHeightEnd;

    @OneToMany(mappedBy = "ideal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Mbti> mbtiList = new ArrayList<>();

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private Boolean smoke;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(value = EnumType.STRING)
    private Religion religion;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer marriagePlan;

    public void addMbtiType(MbtiType mbtiType){
        Mbti mbti = Mbti.builder()
                .mbtiType(mbtiType)
                .ideal(this)
                .build();

        this.mbtiList.add(mbti);
    }

    public List<MbtiType> getMbtiList(){
        return mbtiList.stream()
                .map(Mbti::getMbtiType)
                .collect(Collectors.toList());
    }
}
