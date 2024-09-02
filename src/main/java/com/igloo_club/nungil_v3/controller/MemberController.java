package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.*;
import com.igloo_club.nungil_v3.service.CompanyService;
import com.igloo_club.nungil_v3.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    private final CompanyService companyService;

    @PostMapping("/api/member/essential")
    @Operation(summary = "필수 프로필 등록", description = "사용자 필수 프로필 정보를 전달받아 등록하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404_USER_NOT_FOUND", description = "사용자 조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"404_USER_NOT_FOUND\", \"message\": \"Failed to find the User\"}"))),
    })
    public ResponseEntity<?> createEssentialProfile(@RequestBody EssentialProfileCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.createEssentialProfile(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/member/additional")
    @Operation(summary = "상세 프로필 등록", description = "사용자 상세 프로필 정보를 전달받아 등록하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404_USER_NOT_FOUND", description = "사용자 조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"404_USER_NOT_FOUND\", \"message\": \"Failed to find the User\"}"))),
    })
    public ResponseEntity<?> createAdditionalProfile(@RequestBody AdditionalProfileCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        // 1. 상세 프로필 등록
        memberService.createAdditionalProfile(request, member);
        // 2. 회사 규모 등록
        companyService.registerCompanyScale(request.getScale(), member.getCompany(), member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "근무지 등록", description = "근무지를 회원 정보에 등록하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404_USER_NOT_FOUND", description = "사용자 조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"404_USER_NOT_FOUND\", \"message\": \"Failed to find the User\"}"))),
    })
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
