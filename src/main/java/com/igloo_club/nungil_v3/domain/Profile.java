package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.*;
import com.igloo_club.nungil_v3.dto.AdditionalProfileUpdateRequest;
import lombok.*;
import org.springframework.util.StringUtils;

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

    public void updateAdditonalProfile(AdditionalProfileUpdateRequest request) {

        if (request.getHeight() != null && request.getHeight() >= 0) {
            this.height = request.getHeight();
        }

        if (request.getReligion() != null) {
            this.religion = request.getReligion();
        }

        if (request.getTattoo() != null) {
            this.tattoo = request.getTattoo();
        }

        if (request.getSmoke() != null) {
            this.smoke = request.getSmoke();
        }

        if (request.getMarriagePlan() != null) {
            this.marriagePlan = request.getMarriagePlan();
        }

        if (request.getMbtiType() != null) {
            this.mbtiType = request.getMbtiType();
        }

        if (request.getGrossSalary() != null) {
            this.grossSalary = request.getGrossSalary();
        }

        if (StringUtils.hasText(request.getIntro())) {
            this.intro = request.getIntro();
        }

        if (request.getHobbyList() != null) {
            this.hobbyList.clear();
            request.getHobbyList().forEach(hobbyRequest -> this.addHobby(hobbyRequest.getCategory(), hobbyRequest.getName()));
        }
    }
}
