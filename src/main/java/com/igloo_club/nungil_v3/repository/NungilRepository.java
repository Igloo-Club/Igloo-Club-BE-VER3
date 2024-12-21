package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Nungil;
import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import com.igloo_club.nungil_v3.dto.NungilDetailResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NungilRepository extends JpaRepository<Nungil, Long> {
    Slice<Nungil> findAllByMemberAndStatus(PageRequest request, Member member, NungilStatus status);
    List<Nungil> findAllByMemberAndOpponentAndStatus(Member member, Member receiver, NungilStatus status);
}
