package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.NungilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/nungil")
@RequiredArgsConstructor
public class NungilController {
    private final NungilService nungilService;
    private final MemberService memberService;

    @PostMapping("/recommend")
    public ResponseEntity<NungilResponse> recommendMember(Principal principal){
        Member member = getMember(principal);
        NungilResponse nungilResponse = nungilService.recommendMember(member);
        if (nungilResponse == null){
            //추천할 사용자가 없는 경우
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(nungilResponse);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
