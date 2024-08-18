package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Ideal;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Profile;
import com.igloo_club.nungil_v3.dto.AdditionalProfileCreateRequest;
import com.igloo_club.nungil_v3.dto.IdealCreateRequest;
import com.igloo_club.nungil_v3.dto.LocationCreateRequest;
import com.igloo_club.nungil_v3.dto.EssentialProfileCreateRequest;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.MemberErrorResult;
import com.igloo_club.nungil_v3.repository.CompanyRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final CompanyRepository companyRepository;

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
}
