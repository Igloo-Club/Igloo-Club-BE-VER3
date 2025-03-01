package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import com.igloo_club.nungil_v3.dto.NungilDetailResponse;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.NungilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Nungil", description = "Nungil API")
public class NungilController {
    private final NungilService nungilService;
    private final MemberService memberService;

    @PostMapping("/recommend")
    @Operation(summary = "눈길 뽑기", description = "프로필 뽑을 때 사용하는 api이다")
    public ResponseEntity<NungilResponse> recommendMember(@RequestParam Location location, Principal principal){
        Member member = getMember(principal);
        NungilResponse nungilResponse = nungilService.recommendMember(member, location);
        return ResponseEntity.ok(nungilResponse);
    }

    @GetMapping("/list")
    @Operation(summary = "눈길 리스트 조회", description = "눈길 리스트를 확인할 수 있는 API입니다.")
    public ResponseEntity<Slice<NungilResponse>> getNungilsByMemberAndStatusAndLocation(Principal principal, @RequestParam NungilStatus status, @RequestParam Location location, @PageableDefault(page = 0, size = 4) Pageable pageable){
        Member member = getMember(principal);

        Slice<NungilResponse> nungilPageResponses = null;
        nungilPageResponses = nungilService.getNungilSliceByMemberAndStatus(member, status, location, pageable);

        return ResponseEntity.ok(nungilPageResponses);
    }

    @PostMapping("/send")
    @Operation(summary = "눈길 보내기", description = "눈길을 보내는 api입니다.")
    public ResponseEntity<?> sendNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.sendNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/detail")
    @Operation(summary = "눈길 상세 조회", description = "눈길을 상세 조회하는 API이다.")
    public ResponseEntity<NungilDetailResponse> getNungilDetail(Principal principal, @RequestParam Long nungilId){
        NungilDetailResponse nungilDetailResponse = nungilService.getNungilDetail(nungilId);
        return ResponseEntity.ok(nungilDetailResponse);
    }


    @PatchMapping("/accept")
    @Operation(summary = "눈길 1차 승낙하기", description = "눈길을 1차 승낙하는 api입니다.")
    public ResponseEntity<?> acceptNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.acceptNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/match")
    @Operation(summary = "눈길 최종 승낙하기", description = "눈길을 최종 승낙하는 api입니다.")
    public ResponseEntity<?> matchNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        return ResponseEntity.ok(nungilService.matchNungil(member, nungilId));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "추천된 눈길 수동 삭제", description = "추천 상태의 눈길을 삭제하는 api입니다.")
    public ResponseEntity<?> deleteRecommendedNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.deleteRecommendedNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
