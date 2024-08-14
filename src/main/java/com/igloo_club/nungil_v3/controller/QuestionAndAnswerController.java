package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.QuestionCategory;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerCreateRequest;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerResponse;
import com.igloo_club.nungil_v3.dto.QuestionAndAnswerUpdateRequest;
import com.igloo_club.nungil_v3.dto.QuestionListResponse;
import com.igloo_club.nungil_v3.service.MemberService;
import com.igloo_club.nungil_v3.service.QuestionAndAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionAndAnswerController {

    private final QuestionAndAnswerService questionAndAnswerService;

    private final MemberService memberService;
    @PostMapping
    public ResponseEntity<?> createQuestionAndAnswer(
            @RequestBody QuestionAndAnswerCreateRequest request,
            Principal principal) {

        Member member = getMember(principal);

        questionAndAnswerService.createQuestionAndAnswer(request, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{qaId}")
    public ResponseEntity<?> updateQuestionAndAnswer(
            @RequestBody QuestionAndAnswerUpdateRequest request,
            @PathVariable Long qaId,
            Principal principal) {

        Member member = getMember(principal);

        questionAndAnswerService.updateQuestionAndAnswer(qaId, request, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{qaId}")
    public ResponseEntity<?> getQuestionAndAnswer(
            @PathVariable Long qaId) {

        QuestionAndAnswerResponse response = questionAndAnswerService.getQuestionAndAnswerResponseByMemberAndQuestion(qaId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exposing")
    public ResponseEntity<Slice<QuestionAndAnswerResponse>> getExposingQuestionAndAnswerPageByMember(
            @RequestParam int page,
            @RequestParam int size,
            Principal principal) {

        Member member = getMember(principal);

        Slice<QuestionAndAnswerResponse> response = questionAndAnswerService.getExposingQuestionAndAnswerPageByMember(member, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Slice<QuestionListResponse>> getQuestionList(
            @PathVariable QuestionCategory category,
            @RequestParam int page,
            @RequestParam int size,
            Principal principal) {

        // 멤버 조회 로직 추가
        Member member = getMember(principal);

        Slice<QuestionListResponse> response = questionAndAnswerService.getQuestionList(member, category, page, size);
        return ResponseEntity.ok(response);
    }


    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
