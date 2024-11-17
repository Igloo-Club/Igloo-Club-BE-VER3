package com.igloo_club.nungil_v3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Oauth;
import com.igloo_club.nungil_v3.domain.enums.OauthProvider;
import com.igloo_club.nungil_v3.dto.FCMMessageDTO;
import com.igloo_club.nungil_v3.dto.FCMSendDTO;
import com.igloo_club.nungil_v3.exception.FCMErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.QuestionAndAnswerErrorResult;
import com.igloo_club.nungil_v3.exception.TokenErrorResult;
import com.igloo_club.nungil_v3.repository.OauthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMService {

    private final OauthRepository oauthRepository;

    /**
     * 푸시 메시지 처리를 수행하는 비즈니스 로직
     *
     * @param fcmSendDto 모바일에서 전달받은 Object
     * @return 성공(1), 실패(0)
     */
    public void sendMessageTo(FCMSendDTO fcmSendDto, Member member) throws IOException {

        String message = makeMessage(fcmSendDto, member);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity entity = new HttpEntity<>(message, headers);

        String API_URL = "https://fcm.googleapis.com/v1/projects/fcmfornungil/messages:send";

        try{
            restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
        }catch(HttpClientErrorException ex){
            // 에러 발생 시
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // 에러 메시지를 파싱
            String errorMessage = ex.getResponseBodyAsString();
            System.err.println("FCM Push Request Failed: " + errorMessage);

                // 토큰 부재
                if (errorMessage.contains("Recipient of the message is not set")) {
                    throw new GeneralException(FCMErrorResult.NO_FCM_TOKEN_IN_DB);
                }

                // 잘못된 토큰
                if (errorMessage.contains("The registration token is not a valid FCM registration token")) {
                    throw new GeneralException(FCMErrorResult.FCM_TOKEN_NOT_VALID);
                }

                throw new RuntimeException("FCM Push failed: " + errorMessage, ex);
            } else {
                throw ex;
            }
        }
    }

    /**
     * Firebase Admin SDK의 비공개 키를 참조하여 Bearer 토큰을 발급 받습니다.
     *
     * @return Bearer token
     */
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/fcmfornungil-adminsdk.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    /**
     * FCM 전송 정보를 기반으로 메시지를 구성합니다. (Object -> String)
     *
     * @param fcmSendDto FcmSendDto
     * @return String
     */
    private String makeMessage(FCMSendDTO fcmSendDto, Member member) throws JsonProcessingException {

        Oauth oauth = oauthRepository.findByMemberAndOauthProvider(member, OauthProvider.KAKAO)
                .orElseThrow(()->new GeneralException(TokenErrorResult.UNEXPECTED_TOKEN));
        String fcmToken = oauth.getFcmToken();

        ObjectMapper om = new ObjectMapper();
        FCMMessageDTO fcmMessageDto = FCMMessageDTO.builder()
                .message(FCMMessageDTO.Message.builder()
                        .token(fcmToken)
                        .notification(FCMMessageDTO.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }


    public void updateFCMToken(String fcmToken, Member member) {
        Oauth oauth = oauthRepository.findByMemberAndOauthProvider(member, OauthProvider.KAKAO)
                .orElseThrow(()->new GeneralException(TokenErrorResult.UNEXPECTED_TOKEN));
        oauth.fcmTokenUpdate(fcmToken);
    }
}
