package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import com.igloo_club.nungil_v3.dto.RequiredProfileCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


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

    @Enumerated(value = EnumType.STRING)
    private Location location;

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

    // == 비즈니스 로직 == //
    public void updateRequiredProfile(RequiredProfileCreateRequest request) {
        this.nickname = request.getNickname();
        this.sex = request.getSex();
        this.birthdate = request.getBirthdate();
    }

    public void createDetailedProfile(Profile profile) {
        this.profile = profile;
    }

    public void updateCompany(Company company) {
        this.company = company;
    }
}
