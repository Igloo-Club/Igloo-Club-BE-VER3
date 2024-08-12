package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import com.igloo_club.nungil_v3.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public void registerCompanyScale(CompanyScale scale, Company company, Member member) {

        // 1. 회사 규모가 이미 요청한 규모와 동일한 경우, 그대로 둠
        if (scale == null || scale.equals(company.getScale())) {
            return;
        }

        // 2. 회사 규모가 NULL인 경우, 요청한 규모로 필드만 수정
        if (company.getScale() == null) {
            company.updateScale(scale);
            return;
        }

        // 3. 회사 규모가 요청한 규모와 상이한 경우, DB에 존재하는 데이터를 찾거나 없으면 새로 생성
        String companyName = company.getCompanyName();
        String email = company.getEmail();

        Company companyWithScale = companyRepository.findByCompanyNameAndEmailAndScale(companyName, email, scale)
                        .orElse(company.cloneWithScale(scale));

        member.updateCompany(companyWithScale);

        companyRepository.save(companyWithScale);
    }
}
