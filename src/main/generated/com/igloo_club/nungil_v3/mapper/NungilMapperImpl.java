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
    date = "2024-12-08T18:30:39+0900",
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

        id = nungilOpponentId( nungil );
        nickname = nungilOpponentNickname( nungil );
        birthdate = nungilOpponentBirthdate( nungil );
        companyName = nungilOpponentCompanyCompanyName( nungil );
        height = nungilOpponentProfileHeight( nungil );
        religion = nungilOpponentProfileReligion( nungil );
        tattoo = nungilOpponentProfileTattoo( nungil );
        smoke = nungilOpponentProfileSmoke( nungil );
        marriagePlan = nungilOpponentProfileMarriagePlan( nungil );
        mbtiType = nungilOpponentProfileMbtiType( nungil );

        NungilResponse nungilResponse = new NungilResponse( id, nickname, birthdate, companyName, height, religion, tattoo, smoke, marriagePlan, mbtiType );

        return nungilResponse;
    }

    private Long nungilOpponentId(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Long id = opponent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String nungilOpponentNickname(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        String nickname = opponent.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }

    private LocalDate nungilOpponentBirthdate(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        LocalDate birthdate = opponent.getBirthdate();
        if ( birthdate == null ) {
            return null;
        }
        return birthdate;
    }

    private String nungilOpponentCompanyCompanyName(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Company company = opponent.getCompany();
        if ( company == null ) {
            return null;
        }
        String companyName = company.getCompanyName();
        if ( companyName == null ) {
            return null;
        }
        return companyName;
    }

    private Integer nungilOpponentProfileHeight(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Profile profile = opponent.getProfile();
        if ( profile == null ) {
            return null;
        }
        Integer height = profile.getHeight();
        if ( height == null ) {
            return null;
        }
        return height;
    }

    private Religion nungilOpponentProfileReligion(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Profile profile = opponent.getProfile();
        if ( profile == null ) {
            return null;
        }
        Religion religion = profile.getReligion();
        if ( religion == null ) {
            return null;
        }
        return religion;
    }

    private Boolean nungilOpponentProfileTattoo(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Profile profile = opponent.getProfile();
        if ( profile == null ) {
            return null;
        }
        Boolean tattoo = profile.getTattoo();
        if ( tattoo == null ) {
            return null;
        }
        return tattoo;
    }

    private Boolean nungilOpponentProfileSmoke(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Profile profile = opponent.getProfile();
        if ( profile == null ) {
            return null;
        }
        Boolean smoke = profile.getSmoke();
        if ( smoke == null ) {
            return null;
        }
        return smoke;
    }

    private Integer nungilOpponentProfileMarriagePlan(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Profile profile = opponent.getProfile();
        if ( profile == null ) {
            return null;
        }
        Integer marriagePlan = profile.getMarriagePlan();
        if ( marriagePlan == null ) {
            return null;
        }
        return marriagePlan;
    }

    private MbtiType nungilOpponentProfileMbtiType(Nungil nungil) {
        if ( nungil == null ) {
            return null;
        }
        Member opponent = nungil.getOpponent();
        if ( opponent == null ) {
            return null;
        }
        Profile profile = opponent.getProfile();
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
