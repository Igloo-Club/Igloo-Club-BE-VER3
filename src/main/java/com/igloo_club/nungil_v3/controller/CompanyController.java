package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.CompanyEmailRequest;
import com.igloo_club.nungil_v3.dto.CompanyVerificationRequest;
import com.igloo_club.nungil_v3.service.CompanyService;
import com.igloo_club.nungil_v3.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    private final MemberService memberService;

    /**
     * 주어진 회사 이메일로 인증 확인 메일을 보내는 메서드이다.
     * @param request 회사 이메일이 포함된 DTO
     */
    @PostMapping("/api/company/email")
    public ResponseEntity<?> sendAuthEmail(@RequestBody CompanyEmailRequest request, Principal principal) {
        Member member = getMember(principal);

        companyService.sendAuthEmail(request.getEmail(), member);

        return ResponseEntity.ok(null);
    }

    /**
     * 인증번호를 검증하고, 성공 시 회사 정보를 저장하는 메서드이다.
     * @param request 인증번호 검증 요청 DTO
     * @param principal 회원 정보가 담긴 Principal 객체
     */
    @PostMapping("/api/company/verification")
    public ResponseEntity<?> verifyAuthCode(@RequestBody CompanyVerificationRequest request, Principal principal) {
        Member member = getMember(principal);

        companyService.verifyAuthCode(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
