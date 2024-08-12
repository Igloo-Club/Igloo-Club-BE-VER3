package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {


    Optional<Company> findByCompanyNameAndEmailAndScale(String companyName, String email, CompanyScale scale);
}
