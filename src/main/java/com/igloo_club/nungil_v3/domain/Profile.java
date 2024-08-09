package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.Mbti;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import com.igloo_club.nungil_v3.domain.enums.WorkArrangement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer height;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(value = EnumType.STRING)
    private Religion religion;

    @Column
    private Boolean tattoo;

    @Column
    private Boolean smoke;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer marriagePlan;

    @Column(columnDefinition = "VARCHAR(4)")
    @Enumerated(value = EnumType.STRING)
    private Mbti mbti;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer grossSalary;

    @Column(columnDefinition = "VARCHAR(255)")
    private String job;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkArrangementEntity> workArrangementEntityList = new ArrayList<>();

    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String intro;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hobby> hobbyList = new ArrayList<>();

    public List<WorkArrangement> getWorkArrangementList() {
        return workArrangementEntityList.stream()
                .map(WorkArrangementEntity::getWorkArrangement)
                .collect(Collectors.toList());
    }
}
