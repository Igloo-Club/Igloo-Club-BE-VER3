package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.BlockedMember;
import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockedMemberRepository extends JpaRepository<BlockedMember, Long> {
    List<BlockedMember> findByMember(Member member);

    Optional<BlockedMember> findByMemberAndOpponent(Member member, Member opponent);

}
