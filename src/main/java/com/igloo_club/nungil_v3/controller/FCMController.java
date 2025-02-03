package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.ApiResponseWrapper;
import com.igloo_club.nungil_v3.dto.FCMSendDTO;
import com.igloo_club.nungil_v3.exception.FCMErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.FCMService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "FCM", description = "FCM API")
public class FCMController {

    private final FCMService fcmService;

    private final MemberService memberService;


    @PostMapping("/api/fcm/send")
    public ResponseEntity<ApiResponseWrapper<Object>> pushMessage(@RequestBody @Validated FCMSendDTO fcmSendDto, Principal principal) throws IOException {
        Member member = getMember(principal);
        fcmService.sendMessageTo(fcmSendDto, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/fcm/refresh")
    public ResponseEntity<?> refreshFCMToken(@CookieValue(value = "fcm_token", required = false) String fcmToken, Principal principal) {
        if (fcmToken == null) {
            throw new GeneralException(FCMErrorResult.FCM_TOKEN_NOT_FOUND);
        }
        Member member = getMember(principal);

        fcmService.updateFCMToken(fcmToken, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
