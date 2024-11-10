package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.CompanyEmailRequest;
import com.igloo_club.nungil_v3.dto.CompanyListResponse;
import com.igloo_club.nungil_v3.dto.CompanyRegisterRequest;
import com.igloo_club.nungil_v3.dto.CompanyVerificationRequest;
import com.igloo_club.nungil_v3.service.CompanyService;
import com.igloo_club.nungil_v3.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Company", description = "Company API")
public class CompanyController {

    private final CompanyService companyService;

    private final MemberService memberService;

    /**
     * 주어진 회사 이메일로 인증 확인 메일을 보내는 메서드이다.
     * @param request 회사 이메일이 포함된 DTO
     */
    @PostMapping("/api/company/email")
    @Operation(summary = "회사 인증메일 발송", description = "전달 받은 회사 이메일로 인증 확인 메일을 보내는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400_UNAVAILABLE_EMAIL", description = "사용이 불가능한 이메일(e.g. gmail.com)인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"UNAVAILABLE_EMAIL\", \"message\": \"Given email is not available\"}"))),
            @ApiResponse(responseCode = "400_DUPLICATED_EMAIL", description = "이미 회원가입을 마친 이메일이 존재하는 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"DUPLICATED_EMAIL\", \"message\": \"Given email is already in use\"}"))),
            @ApiResponse(responseCode = "400_ALREADY_USING", description = "이미 회원가입을 마친 이메일이 본인의 것인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"ALREADY_USING\", \"message\": \"You are already using this email\"}"))),
            @ApiResponse(responseCode = "500_MESSAGING_EXCEPTION", description = "이메일 전송 중 에러 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"MESSAGING_EXCEPTION\", \"message\": \"Problem occurered when sending email\"}"))),
    })
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
    @Operation(summary = "회사 인증번호 검증", description = "인증번호가 유효한지 검증하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400_UNAVAILABLE_EMAIL", description = "사용이 불가능한 이메일(e.g. gmail.com)인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"UNAVAILABLE_EMAIL\", \"message\": \"Given email is not available\"}"))),
            @ApiResponse(responseCode = "400_DUPLICATED_EMAIL", description = "이미 회원가입을 마친 이메일이 존재하는 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"DUPLICATED_EMAIL\", \"message\": \"Given email is already in use\"}"))),
            @ApiResponse(responseCode = "400_ALREADY_USING", description = "이미 회원가입을 마친 이메일이 본인의 것인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"ALREADY_USING\", \"message\": \"You are already using this email\"}"))),
            @ApiResponse(responseCode = "400_REDIS_NOT_FOUND", description = "요청된 회사 이메일에 대한 인증번호가 없거나 만료된 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"REDIS_NOT_FOUND\", \"message\": \"The requested data is not available or has expired\"}"))),
            @ApiResponse(responseCode = "400_WRONG_AUTH_CODE", description = "주어진 인증번호가 틀린 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"WRONG_AUTH_CODE\", \"message\": \"The provided authentication code is incorrect\"}"))),
    })
    public ResponseEntity<?> verifyAuthCode(@RequestBody CompanyVerificationRequest request, Principal principal) {
        Member member = getMember(principal);

        companyService.verifyAuthCode(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 주어진 이메일을 사용하는 회사명 목록을 반환하는 메서드이다.
     * @param email 회사 이메일
     * @return 회사명 목록
     */
    @GetMapping("/api/company")
    @Operation(summary = "회사명 조회", description = "전달받은 이메일에 해당하는 회사명 리스트를 조회하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompanyListResponse.class))),
            @ApiResponse(responseCode = "400_UNAVAILABLE_EMAIL", description = "사용이 불가능한 이메일(e.g. gmail.com)인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"UNAVAILABLE_EMAIL\", \"message\": \"Given email is not available\"}"))),
    })
    public ResponseEntity<CompanyListResponse> getCompanyList(
            @Parameter(description = "회사 이메일 혹은 도메인 주소", example = "soongsil.ac.kr")
                @RequestParam String email) {
        CompanyListResponse companyList = companyService.getCompanyList(email);
        return ResponseEntity.ok(companyList);
    }

    @PostMapping("/api/company")
    @Operation(summary = "회사명 등록", description = "사용자에게 회사명을 등록하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400_UNAVAILABLE_EMAIL", description = "사용이 불가능한 이메일(e.g. gmail.com)인 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"UNAVAILABLE_EMAIL\", \"message\": \"Given email is not available\"}"))),
            @ApiResponse(responseCode = "400_UNAUTHENTICATED_EMAIL", description = "이메일 검증 API에서 인증된 이메일과 다른 이메일로 요청을 보낸 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"code\": \"UNAUTHENTICATED_EMAIL\", \"message\": \"Given email is not authenticated\"}"))),
    })
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRegisterRequest request, Principal principal) {
        Member member = getMember(principal);

        companyService.registerCompany(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
