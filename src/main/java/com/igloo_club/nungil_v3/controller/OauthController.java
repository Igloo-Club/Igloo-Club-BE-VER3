package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.LoginResponse;
import com.igloo_club.nungil_v3.dto.OauthLoginRequest;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.TokenErrorResult;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.OauthService;
import com.igloo_club.nungil_v3.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    private final TokenService tokenService;

    private final MemberService memberService;

    @PostMapping("/api/auth/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody OauthLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = oauthService.kakaoLogin(loginRequest.getCode(), request, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<LoginResponse> createNewAccessToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new GeneralException(TokenErrorResult.REFRESH_TOKEN_NOT_FOUND);
        }

        LoginResponse newLoginResponse = tokenService.createNewAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newLoginResponse);
    }

    @PostMapping("/api/logout/kakao")
    public ResponseEntity<?> kakaoLogout(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        Member member = getMember(principal);
        oauthService.kakaoLogout(member, request, response);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
