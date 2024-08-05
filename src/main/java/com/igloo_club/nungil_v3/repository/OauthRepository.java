package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Oauth;
import com.igloo_club.nungil_v3.domain.enums.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthRepository extends JpaRepository<Oauth, Long> {
    Optional<Oauth> findByOauthId(String oauthId);

    Optional<Oauth> findByMemberAndOauthProvider(Member member, OauthProvider oauthProvider);
}
