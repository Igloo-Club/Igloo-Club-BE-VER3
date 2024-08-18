package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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
    private MbtiType mbtiType;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer grossSalary;

    @Column(columnDefinition = "VARCHAR(255)")
    private String job;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkArrangementEntity> workArrangementEntityList = new ArrayList<>();

    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String intro;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Hobby> hobbyList = new ArrayList<>();

    public List<WorkArrangement> getWorkArrangementList() {
        return workArrangementEntityList.stream()
                .map(WorkArrangementEntity::getWorkArrangement)
                .collect(Collectors.toList());
    }

    public void addWorkArrangement(WorkArrangement workArrangement) {
        WorkArrangementEntity workArrangementEntity = WorkArrangementEntity.builder()
                .workArrangement(workArrangement)
                .profile(this)
                .build();

        this.workArrangementEntityList.add(workArrangementEntity);
    }

    public void addHobby(HobbyCategory category, HobbyName name) {
        Hobby hobby = Hobby.builder()
                .category(category)
                .name(name)
                .profile(this)
                .build();

        this.hobbyList.add(hobby);
    }
}
