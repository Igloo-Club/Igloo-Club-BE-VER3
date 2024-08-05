package com.igloo_club.nungil_v3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igloo_club.nungil_v3.config.jwt.TokenProvider;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Oauth;
import com.igloo_club.nungil_v3.domain.RefreshToken;
import com.igloo_club.nungil_v3.domain.enums.OauthProvider;
import com.igloo_club.nungil_v3.domain.enums.RegisterProgress;
import com.igloo_club.nungil_v3.dto.LoginResponse;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.GlobalErrorResult;
import com.igloo_club.nungil_v3.exception.TokenErrorResult;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import com.igloo_club.nungil_v3.repository.OauthRepository;
import com.igloo_club.nungil_v3.repository.RefreshTokenRepository;
import com.igloo_club.nungil_v3.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Service
@Transactional(readOnly = true)
public class OauthService {

    private final OauthRepository oauthRepository;

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final String CLIENT_ID;

    private final String REDIRECT_URI;

    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30L);

    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    @Autowired
    public OauthService(MemberRepository memberRepository,
                        OauthRepository oauthRepository,
                        TokenProvider tokenProvider,
                        RefreshTokenRepository refreshTokenRepository,
                        @Value("${OAuth2.kakao.client-id}") String CLIENT_ID,
                        @Value("${OAuth2.kakao.redirect-uri}") String REDIRECT_URI) {
        this.memberRepository = memberRepository;
        this.oauthRepository = oauthRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.CLIENT_ID = CLIENT_ID;
        this.REDIRECT_URI = REDIRECT_URI;
    }


    @Transactional
    public LoginResponse kakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        // 1. 인가 코드로 OAuth2 액세스 토큰 요청
        String oauthAccessToken = getAccessToken(code);

        // 2. OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        // 3. 회원 정보 저장
        Member member = registerKakaoUser(responseJson, oauthAccessToken);

        // 3-1. 회원 프로필 등록 여부 판별
        RegisterProgress nextProgress = getNextProgress(member);

        // 4. JWT 리프레시 토큰 발급
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // 5. JWT 액세스 토큰 발급
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

        return new LoginResponse(accessToken, nextProgress.getTitle(), RegisterProgress.REGISTERED.equals(nextProgress));
    }

    /**
     * 주어진 회원이 수행해야 하는 다음 가입 절차를 반환한다.
     * @param member 회원 엔티티
     * @return 다음에 수행할 가입 절차
     */
    public RegisterProgress getNextProgress(Member member) {

        // 닉네임 존재하면, 가입 완료
        if (member.getNickname() != null) {
            return RegisterProgress.REGISTERED;
        }

        // 성별 생년월일이 존재하면, 닉네임 입력부터
        if (member.getBirthdate() != null) {
            return RegisterProgress.NICKNAME_INPUT;
        }

        // 회사 이메일 인증 후이면, 성별 생년월일 입력부터
        if (member.getCompany() != null) {
            return RegisterProgress.GENDER_BIRTHDATE_INPUT;
        }

        // 회사 이메일 입력 후이면, 회사 이메일 입력부터
        if (member.getEmail() != null) {
            return RegisterProgress.COMPANY_EMAIL_INPUT;
        }

        // 회사 이메일 입력 전이면, 회사 이메일 입력부터
        return RegisterProgress.COMPANY_EMAIL_INPUT;
    }

    /**
     * 사용자를 로그아웃 처리하는 메서드이다.
     * @param member 로그아웃할 사용자
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void kakaoLogout(Member member, HttpServletRequest request, HttpServletResponse response) {
        // 1. 리프레시 토큰 만료
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);

        // 2. 사용자의 카카오 액세스 토큰 조회
        Oauth oauth = oauthRepository.findByMemberAndOauthProvider(member, OauthProvider.KAKAO)
                .orElseThrow(()->new GeneralException(TokenErrorResult.UNEXPECTED_TOKEN));
        String oauthAccessToken = oauth.getOauthAccessToken();

        // 3. 카카오로 로그아웃 요청 보내기
        logoutKakao(oauthAccessToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addHttpOnlyCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    /**
     * JWT 리프레시 토큰을 데이터베이스에 저장하는 메서드이다.
     * @param memberId 리프레시 토큰에 대응되는 회원 ID
     * @param newRefreshToken 새로운 리프레시 토큰
     */
    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 인가 코드로 카카오 서버에 액세스 토큰을 요청하는 메서드이다.
     * @param code 인가 코드
     * @return 액세스 토큰
     */
    private String getAccessToken(String code) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
//        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        // HTTP 응답에서 액세스 토큰 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new GeneralException(GlobalErrorResult.JSON_PROCESSING_EXCEPTION, e);
        }
        return jsonNode.get("access_token").asText();
    }

    /**
     * 액세스 토큰으로 카카오 서버에 회원 정보를 요청하는 메서드이다.
     * @param accessToken 액세스 토큰
     * @return JSON 형식의 회원 정보
     */
    private JsonNode getKakaoUserInfo(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                userInfoRequest,
                String.class
        );

        // HTTP 응답 반환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new GeneralException(GlobalErrorResult.JSON_PROCESSING_EXCEPTION, e);
        }
    }

    /**
     * 카카오 회원 정보를 데이터베이스에 저장하는 메서드이다.
     * @param responseJson JSON 형식의 카카오 회원 정보
     * @return 저장된 Member 객체
     */
    private Member registerKakaoUser(JsonNode responseJson, String oauthAccess) {
        String oauthId = responseJson.get("id").asText();

        Oauth oauth = oauthRepository.findByOauthId(oauthId)
                .map(entity -> entity.update(oauthAccess))
                .orElse(Oauth.builder()
                        .oauthProvider(OauthProvider.KAKAO)
                        .oauthId(oauthId)
                        .build());

        Member member = oauth.getMember();

        if(member == null){
            member = Member.builder().build();
            oauth.memberUpdate(member);
        }
        oauthRepository.save(oauth);
        return memberRepository.save(member);
    }

    /**
     * 카카오 액세스 토큰을 사용하여, 카카오 서버에 로그아웃 요청을 보내는 메서드이다.
     * @param accessToken 카카오 액세스 토큰
     */
    private void logoutKakao(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> logoutRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                logoutRequest,
                String.class
        );
    }
}

