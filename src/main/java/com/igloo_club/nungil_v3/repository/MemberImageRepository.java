package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.MemberImage;
import com.igloo_club.nungil_v3.domain.enums.ImageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    Optional<MemberImage> findTopByMemberAndFilename(Member member, UUID filename);

    Long countByMemberAndStatus(Member member, ImageStatus uploadComplete);
}
