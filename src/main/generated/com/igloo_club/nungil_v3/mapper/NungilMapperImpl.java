package com.igloo_club.nungil_v3.mapper;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Nungil;
import com.igloo_club.nungil_v3.domain.Profile;
import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import com.igloo_club.nungil_v3.domain.enums.Religion;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-14T17:38:21+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.12 (Microsoft)"
)
public class NungilMapperImpl implements NungilMapper {

    @Override
    public NungilResponse toResponse(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }

        Long id = null;
        String nickname = null;
        LocalDate birthdate = null;
        String companyName = null;
        Integer height = null;
        Religion religion = null;
        Boolean tattoo = null;
        Boolean smoke = null;
        Integer marriagePlan = null;
        MbtiType mbtiType = null;

        id = nungilMemberId( nungil );
        nickname = nungilMemberNickname( nungil );
        birthdate = nungilMemberBirthdate( nungil );
        companyName = nungilMemberCompanyCompanyName( nungil );
        height = nungilMemberProfileHeight( nungil );
        religion = nungilMemberProfileReligion( nungil );
        tattoo = nungilMemberProfileTattoo( nungil );
        smoke = nungilMemberProfileSmoke( nungil );
        marriagePlan = nungilMemberProfileMarriagePlan( nungil );
        mbtiType = nungilMemberProfileMbtiType( nungil );

        NungilResponse nungilResponse = new NungilResponse( id, nickname, birthdate, companyName, height, religion, tattoo, smoke, marriagePlan, mbtiType );

        return nungilResponse;
    }

    private Long nungilMemberId(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Long id = member.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String nungilMemberNickname(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        String nickname = member.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }

    private LocalDate nungilMemberBirthdate(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        LocalDate birthdate = member.getBirthdate();
        if ( birthdate == null ) {
            return null;
        }
        return birthdate;
    }

    private String nungilMemberCompanyCompanyName(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Company company = member.getCompany();
        if ( company == null ) {
            return null;
        }
        String companyName = company.getCompanyName();
        if ( companyName == null ) {
            return null;
        }
        return companyName;
    }

    private Integer nungilMemberProfileHeight(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Profile profile = member.getProfile();
        if ( profile == null ) {
            return null;
        }
        Integer height = profile.getHeight();
        if ( height == null ) {
            return null;
        }
        return height;
    }

    private Religion nungilMemberProfileReligion(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Profile profile = member.getProfile();
        if ( profile == null ) {
            return null;
        }
        Religion religion = profile.getReligion();
        if ( religion == null ) {
            return null;
        }
        return religion;
    }

    private Boolean nungilMemberProfileTattoo(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Profile profile = member.getProfile();
        if ( profile == null ) {
            return null;
        }
        Boolean tattoo = profile.getTattoo();
        if ( tattoo == null ) {
            return null;
        }
        return tattoo;
    }

    private Boolean nungilMemberProfileSmoke(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Profile profile = member.getProfile();
        if ( profile == null ) {
            return null;
        }
        Boolean smoke = profile.getSmoke();
        if ( smoke == null ) {
            return null;
        }
        return smoke;
    }

    private Integer nungilMemberProfileMarriagePlan(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Profile profile = member.getProfile();
        if ( profile == null ) {
            return null;
        }
        Integer marriagePlan = profile.getMarriagePlan();
        if ( marriagePlan == null ) {
            return null;
        }
        return marriagePlan;
    }

    private MbtiType nungilMemberProfileMbtiType(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member member = nungil.getMember();
        if ( member == null ) {
            return null;
        }
        Profile profile = member.getProfile();
        if ( profile == null ) {
            return null;
        }
        MbtiType mbtiType = profile.getMbtiType();
        if ( mbtiType == null ) {
            return null;
        }
        return mbtiType;
    }
}
