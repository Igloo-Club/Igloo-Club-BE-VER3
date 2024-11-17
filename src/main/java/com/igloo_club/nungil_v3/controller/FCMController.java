package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.ApiResponseWrapper;
import com.igloo_club.nungil_v3.dto.FCMSendDTO;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class FCMController {

    private final FCMService fcmService;

    private final MemberService memberService;


    @PostMapping("/api/send")
    public ResponseEntity<ApiResponseWrapper<Object>> pushMessage(@RequestBody @Validated FCMSendDTO fcmSendDto, Principal principal) throws IOException {
        Member member = getMember(principal);
        int result = fcmService.sendMessageTo(fcmSendDto, member);

        ApiResponseWrapper<Object> arw = ApiResponseWrapper
                .builder()
                .result(result)
                .resultCode(200)
                .resultMsg("FCM message sent successfully")
                .build();

        return new ResponseEntity<>(arw, HttpStatus.OK);
    }
    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
