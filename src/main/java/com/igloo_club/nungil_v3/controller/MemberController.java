package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.*;
import com.igloo_club.nungil_v3.service.CompanyService;
import com.igloo_club.nungil_v3.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final CompanyService companyService;

    @PostMapping("/api/member/essential")
    public ResponseEntity<?> createEssentialProfile(@RequestBody EssentialProfileCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.createEssentialProfile(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/member/additional")
    public ResponseEntity<?> createAdditionalProfile(@RequestBody AdditionalProfileCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        // 1. 상세 프로필 등록
        memberService.createAdditionalProfile(request, member);
        // 2. 회사 규모 등록
        companyService.registerCompanyScale(request.getScale(), member.getCompany(), member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/member/location")
    public ResponseEntity<?> createLocation(@RequestBody LocationCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.createLocation(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/member/ideal")
    public ResponseEntity<?> createIdeal(@RequestBody IdealCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.createIdeal(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/member/ideal")
    public ResponseEntity<IdealResponse> getIdeal(Principal principal) {

        Member member = getMember(principal);

        IdealResponse response = memberService.getIdeal(member);

        return ResponseEntity.ok(response);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
