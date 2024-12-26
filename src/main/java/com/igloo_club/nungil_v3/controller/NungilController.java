package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import com.igloo_club.nungil_v3.dto.NungilDetailResponse;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.NungilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(nungilResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<Slice<NungilResponse>> getNungilsByMemberAndStatus(Principal principal, @RequestParam NungilStatus status, @PageableDefault(page = 0, size = 4) Pageable pageable){
        Member member = getMember(principal);

        Slice<NungilResponse> nungilPageResponses = null;
        nungilPageResponses = nungilService.getNungilSliceByMemberAndStatus(member, status, pageable);

        return ResponseEntity.ok(nungilPageResponses);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.sendNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<NungilDetailResponse> getNungilDetail(Principal principal, @RequestParam Long nungilId){
        NungilDetailResponse nungilDetailResponse = nungilService.getNungilDetail(nungilId);
        return ResponseEntity.ok(nungilDetailResponse);
    }


    @PatchMapping("/accept")
    public ResponseEntity<?> acceptNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.acceptNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/match")
    public ResponseEntity<?> matchNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.matchNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRecommendedNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.deleteRecommendedNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
