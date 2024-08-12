package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Profile;
import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.dto.DetailedProfileCreateRequest;
import com.igloo_club.nungil_v3.dto.LocationCreateRequest;
import com.igloo_club.nungil_v3.dto.RequiredProfileCreateRequest;
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
    public void createRequiredProfile(RequiredProfileCreateRequest request, Member member) {
        member.updateRequiredProfile(request);
    }

    @Transactional
    public void createDetailedProfile(DetailedProfileCreateRequest request, Member member) {

        Profile profile = request.toProfile();
        member.createDetailedProfile(profile);
    }

    @Transactional
    public void createLocation(LocationCreateRequest request, Member member) {
        member.addLocation(request.getLocation());
    }
}
