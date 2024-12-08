package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Nungil;
import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NungilRepository extends JpaRepository<Nungil, Long> {
    Slice<Nungil> findAllByMemberAndStatus(PageRequest request, Member member, NungilStatus status);

}
