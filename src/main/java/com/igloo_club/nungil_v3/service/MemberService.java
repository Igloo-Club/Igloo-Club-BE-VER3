package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Ideal;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Profile;
import com.igloo_club.nungil_v3.dto.*;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.IdealErrorResult;
import com.igloo_club.nungil_v3.exception.MemberErrorResult;
import com.igloo_club.nungil_v3.repository.CompanyRepository;
import com.igloo_club.nungil_v3.repository.IdealRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final IdealRepository idealRepository;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorResult.USER_NOT_FOUND));
    }

    @Transactional
    public void createEssentialProfile(EssentialProfileCreateRequest request, Member member) {
        member.createEssentialProfile(request);
    }

    @Transactional
    public void createAdditionalProfile(AdditionalProfileCreateRequest request, Member member) {

        Profile profile = request.toProfile();
        member.createAdditionalProfile(profile);
    }

    @Transactional
    public void createLocation(LocationCreateRequest request, Member member) {
        member.addLocation(request.getLocation());
    }

    @Transactional
    public void createIdeal(IdealCreateRequest request, Member member) {

        Ideal ideal = request.toIdeal();
        member.createIdeal(ideal);
    }

    public IdealResponse getIdeal(Member member) {

        Ideal ideal = idealRepository.findById(member.getIdeal().getId())
                        .orElseThrow(()->new GeneralException(IdealErrorResult.IDEAL_NOT_FOUND));
        return IdealResponse.create(ideal);
    }
}

