package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
        Optional<RefreshToken> findByMemberId(Long memberId);
        Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
