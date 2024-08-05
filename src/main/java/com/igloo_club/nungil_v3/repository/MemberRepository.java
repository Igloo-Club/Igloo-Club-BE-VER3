package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
