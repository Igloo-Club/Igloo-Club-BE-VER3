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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "선호 이상 등록", description = "선호 이상을 등록하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400_IDEAL_AGE_PARADOX", description = "이상형의 preferredAgeStart > preferredAgeEnd 인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"IDEAL_AGE_PARADOX\", \"message\": \"Ideal age start is bigger than end\"}"))),
            @ApiResponse(responseCode = "400_IDEAL_HEIGHT_PARADOX", description = "이상형의 preferredHeightStart> preferredHeightEnd인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"IDEAL_HEIGHT_PARADOX\", \"message\": \"Ideal height start is bigger than end\"}"))),
    })
    @PostMapping("/api/member/ideal")
    public ResponseEntity<?> createIdeal(@RequestBody IdealCreateRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.createIdeal(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "선호 이상 조회", description = "선호 이상을 조회하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400_IDEAL_NOT_FOUND", description = "이상형이 존재하지 않을 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"IDEAL_NOT_FOUND\", \"message\": \"Failed to find the Ideal\"}"))),
    })
    @GetMapping("/api/member/ideal")
    public ResponseEntity<IdealResponse> getIdeal(Principal principal) {

        Member member = getMember(principal);

        IdealResponse response = memberService.getIdeal(member);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이미지 업로드 Presigned URL 발급", description = "이미지 업로드를 위한 URL을 발급하는 API 입니다. 이미지 확장자는 `png` 고정입니다.\n성공 시 응답 값의 URL로, 이미지 파일과 함께 `PUT` 요청을 보내면 됩니다.\n만약 PUT 요청이 성공/실패한 경우, 반드시 업로드 완료 알림 API를 호출해주세요.", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/api/member/images")
    public ResponseEntity<?> getImageUploadUrl(Principal principal) {
        Member member = getMember(principal);

        MemberImageUploadUrlCreateResponse response = memberService.getImageUploadUrl(member);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이미지 업로드 완료 알림", description = "Presigned URL을 통한 이미지 업로드 성공, 혹은 실패에 대해 서버에 알려주는 API입니다.\n이미지 업로드 Presigned URL 발급 API를 통해 발급받은 Presigned URL을 사용한 이후에 반드시 그 결과를 알려주세요!", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/api/member/images/notify")
    public ResponseEntity<?> notifyImageUpload(@RequestBody ImageUploadNotifyRequest request, Principal principal) {
        Member member = getMember(principal);

        memberService.notifyImageUpload(request.getFilename(), request.getStatus(), member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "본인 프로필 조회", description = "API 요청자의 프로필을 조회하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/api/member")
    public ResponseEntity<?> getMemberProfile(Principal principal) {
        Member member = getMember(principal);

        MemberProfileResponse response = memberService.getMemberProfile(member);

        return ResponseEntity.ok(response);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
