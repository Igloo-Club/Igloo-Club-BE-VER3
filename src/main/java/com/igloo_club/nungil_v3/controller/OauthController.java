package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.LoginResponse;
import com.igloo_club.nungil_v3.dto.OauthLoginRequest;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.TokenErrorResult;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.OauthService;
import com.igloo_club.nungil_v3.service.TokenService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "OAuth API")
public class OauthController {
    private final OauthService oauthService;

    private final TokenService tokenService;

    private final MemberService memberService;

    @PostMapping("/api/auth/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 사용하여 로그인하는 API")
    public ResponseEntity<LoginResponse> kakaoLogin(
            @RequestBody
            OauthLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = oauthService.kakaoLogin(loginRequest.getCode(), request, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/api/auth/refresh")
    @Operation(summary = "액세스 토큰 재발급", description = "쿠키에 저장된 리프레시 토큰으로 액세스 토큰을 재발급하는 API <br>파라미터 필요없이 execute 하면 됩니다")
    public ResponseEntity<LoginResponse> createNewAccessToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new GeneralException(TokenErrorResult.REFRESH_TOKEN_NOT_FOUND);
        }

        LoginResponse newLoginResponse = tokenService.createNewAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newLoginResponse);
    }

    @Hidden
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
