package com.igloo_club.nungil_v3.controller;

import com.igloo_club.nungil_v3.config.jwt.TokenProvider;
import com.igloo_club.nungil_v3.domain.ChatDTO;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.ChatMessageResponse;
import com.igloo_club.nungil_v3.dto.ChatRoomDetailResponse;
import com.igloo_club.nungil_v3.dto.ChatRoomListResponse;
import com.igloo_club.nungil_v3.service.ChatMessageService;
import com.igloo_club.nungil_v3.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.igloo_club.nungil_v3.util.TokenUtil.HEADER_AUTHORIZATION;
import static com.igloo_club.nungil_v3.util.TokenUtil.getAccessToken;


@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @MessageMapping("/send")
    public void send(@Payload ChatDTO chatDTO, @Header(HEADER_AUTHORIZATION) String authorizationHeader) {
        String accessToken = getAccessToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Member member = getMember(authentication);

        ChatDTO mappedChat = chatMessageService.save(chatDTO, member);

        messagingTemplate.convertAndSend("/topic/" + chatDTO.getChatRoomId(), mappedChat);
    }

    @GetMapping("/api/chatroom/{chatRoomId}")
    public ResponseEntity<ChatRoomDetailResponse> getMessageSlice(@PathVariable Long chatRoomId,
                                                                  @RequestParam(defaultValue = "0") int pageNumber,
                                                                  @RequestParam(defaultValue = "12") int pageSize,
                                                                  Principal principal) {

        Member member = getMember(principal);
        // 메시지를 최근에 작성된 순서대로 조회
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc("createdAt")));

        ChatRoomDetailResponse chatRoomDetail = chatMessageService.getChatRoomDetail(chatRoomId, member, pageRequest);

        return new ResponseEntity<>(chatRoomDetail, HttpStatus.OK);
    }

    @GetMapping("/api/chatroom")
    public ResponseEntity<Slice<ChatRoomListResponse>> getRoomSlice(@RequestParam(defaultValue = "0") int pageNumber,
                                                                    @RequestParam(defaultValue = "12") int pageSize,
                                                                    Principal principal) {

        Member member = getMember(principal);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "lastMessageAt"));

        Slice<ChatRoomListResponse> roomSlice = chatMessageService.getChatRoomSlice(member, pageRequest);

        return new ResponseEntity<>(roomSlice, HttpStatus.OK);
    }

    @DeleteMapping("/api/chat/{chatRoomId}/{chatMessageId}")
    public ResponseEntity<ChatMessageResponse> deleteMessage(@PathVariable Long chatRoomId, @PathVariable Long chatMessageId, Principal principal) {
        Member member = getMember(principal);

        ChatMessageResponse response = chatMessageService.deleteMessage(chatRoomId, chatMessageId, member);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/api/chatroom/{chatRoomId}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Long chatRoomId, Principal principal) {
        Member member = getMember(principal);

        chatMessageService.deleteChatRoom(chatRoomId, member);

        return ResponseEntity.noContent().build();
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
