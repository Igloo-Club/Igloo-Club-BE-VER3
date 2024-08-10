package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.DetailedProfileCreateRequest;
import com.igloo_club.nungil_v3.dto.RequiredProfileCreateRequest;
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
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member/required")
    public ResponseEntity<?> createRequiredProfile(@RequestBody RequiredProfileCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.createRequiredProfile(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/member/detailed")
    public ResponseEntity<?> createDetailedProfile(@RequestBody DetailedProfileCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.createDetailedProfile(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
