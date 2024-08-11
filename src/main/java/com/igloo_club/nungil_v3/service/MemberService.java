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

        // 1. 상세 프로필 등록
        Profile profile = request.toProfile();
        member.createDetailedProfile(profile);


        // 2. 회사 규모 등록
        CompanyScale scale = request.getScale();
        if (scale != null) {
            Company company = member.getCompany();
            Company updatedCompany = company.updateOrCreateCompanyWithScale(scale);
            member.updateCompany(updatedCompany);

            companyRepository.save(updatedCompany);
        }
    }

    @Transactional
    public void createLocation(LocationCreateRequest request, Member member) {
        member.addLocation(request.getLocation());
    }
}
