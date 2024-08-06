package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCompanyNameAndEmail(String companyName, String Email);
}
