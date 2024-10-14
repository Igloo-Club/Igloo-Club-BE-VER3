package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import com.igloo_club.nungil_v3.dto.EssentialProfileCreateRequest;
import com.igloo_club.nungil_v3.dto.IdealResponse;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.IdealErrorResult;
import com.igloo_club.nungil_v3.exception.MemberErrorResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
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

    @Column(columnDefinition = "VARCHAR(255)")
    private String job;

    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ideal_id")
    private Ideal ideal;

    // == 비즈니스 로직 == //
    public void createEssentialProfile(EssentialProfileCreateRequest request) {
        this.nickname = request.getNickname();
        this.sex = request.getSex();
        this.birthdate = request.getBirthdate();
        this.job = request.getJob();
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

    public void resetDrawCount(){
        this.drawCount = 0L;
    }

    public void plusDrawCount() {this.drawCount += 1L;}

    public Sex getOppositeSex(){
        if (this.getSex().equals(Sex.FEMALE)){return Sex.MALE;}
        if (this.getSex().equals(Sex.MALE)){return Sex.FEMALE;}
        throw new GeneralException(MemberErrorResult.SEXLESS_USER);
    }
    public int calculateAge() {
        LocalDate currentDate = LocalDate.now();
        return Period.between(this.getBirthdate(), currentDate).getYears();
    }
}
