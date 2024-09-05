package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import com.igloo_club.nungil_v3.dto.EssentialProfileCreateRequest;
import com.igloo_club.nungil_v3.dto.IdealResponse;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.IdealErrorResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String email;

    @ElementCollection
    @CollectionTable(name = "location", joinColumns = {
            @JoinColumn(name = "member_id")
    })
    @Column(name = "location_name")
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private List<Location> location = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    private LocalDate birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder.Default
    private Long drawCount = 0L;

    @Builder.Default
    private boolean agreeMarketing = true;

    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ideal_id")
    private Ideal ideal;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberImage> memberImageList;

    // == 비즈니스 로직 == //
    public void createEssentialProfile(EssentialProfileCreateRequest request) {
        this.nickname = request.getNickname();
        this.sex = request.getSex();
        this.birthdate = request.getBirthdate();
    }

    public void createAdditionalProfile(Profile profile) {
        this.profile = profile;
    }

    public void createIdeal(Ideal ideal) { this.ideal = ideal; }

    public void updateCompany(Company company) {
        this.company = company;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void addLocation(Location location) {
        this.location.add(location);
    }

}
