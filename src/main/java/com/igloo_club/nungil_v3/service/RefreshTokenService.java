package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.RefreshToken;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.TokenErrorResult;
import com.igloo_club.nungil_v3.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GeneralException(TokenErrorResult.UNEXPECTED_TOKEN));
    }
}
