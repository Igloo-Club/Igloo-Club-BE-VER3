package com.igloo_club.nungil_v3.listener;

import com.igloo_club.nungil_v3.config.jwt.TokenProvider;
import com.igloo_club.nungil_v3.domain.ChatDTO;
import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.ChatMessageStatus;
import com.igloo_club.nungil_v3.exception.ChatRoomErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.repository.ChatMessageRepository;
import com.igloo_club.nungil_v3.repository.ChatRoomRepository;
import com.igloo_club.nungil_v3.util.RedisKeyManager;
import com.igloo_club.nungil_v3.util.SetRedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.time.Duration;
import java.util.Map;

import static com.igloo_club.nungil_v3.util.TokenUtil.HEADER_AUTHORIZATION;
import static com.igloo_club.nungil_v3.util.TokenUtil.getAccessToken;

@Component
@RequiredArgsConstructor
public class ChatEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final TokenProvider tokenProvider;

    private final SetRedisUtil redisUtil;

    @EventListener
    @Transactional
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 채팅방 ID 추출
        Long chatRoomId = null;
        String destination = headerAccessor.getDestination();
        if (destination != null && destination.startsWith("/topic/chatroom/")) {
            chatRoomId = Long.parseLong(destination.split("/topic/chatroom/")[1]);
        }

        if (chatRoomId == null) {
            return;
        }

        // 입장한 사용자 ID 추출
        Long memberId = null;
        String token = getAccessToken(String.valueOf(headerAccessor.getFirstNativeHeader(HEADER_AUTHORIZATION)));

        if (tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            memberId = Long.parseLong(authentication.getName());
        }

        if (memberId == null) {
            return;
        }

        // Redis에 채팅방에 입장한 사용자 정보 저장
        String key = RedisKeyManager.getChatRoomMemberKey(chatRoomId, memberId);
        String sessionId = headerAccessor.getSessionId();
        redisUtil.add(key, sessionId, Duration.ofHours(3));

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("chatRoomId", chatRoomId);
            sessionAttributes.put("memberId", memberId);
        }

        // 입장 시 상대방의 모든 메시지를 읽음 상태로 변경
        // 1. 상대방이 보낸 "UNREAD" 상태의 메시지를 전부 "READ"로 변경
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ChatRoomErrorResult.CHAT_ROOM_NOT_FOUND));
        Member opponent = chatRoom.getOpponent(memberId);
        chatMessageRepository.updateStatusToReadByMember(opponent);

        // 2. 클라이언트에게 "READ" 상태로 변경됨을 알림
        ChatDTO chatDTO = ChatDTO.of(chatRoomId, opponent, ChatMessageStatus.ALL_READ);
        messagingTemplate.convertAndSend(destination, chatDTO);
    }

    @EventListener
    @Transactional
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null) {
            return;
        }

        // 채팅방 ID 추출
        Long chatRoomId = null;
        String chatroomIdStr = String.valueOf(sessionAttributes.get("chatRoomId"));
        if (!StringUtils.isNumeric(chatroomIdStr)) {
            throw new RuntimeException("chatroom Id not found");
        }
        chatRoomId = Long.valueOf(chatroomIdStr);

        // 입장한 사용자 ID 추출
        Long memberId = null;
        String memberIdStr = String.valueOf(sessionAttributes.get("memberId"));
        if (!StringUtils.isNumeric(memberIdStr)) {
            throw new RuntimeException("member Id not found");
        }
        memberId = Long.valueOf(memberIdStr);

        // Redis에서 채팅방에 퇴장한 사용자 정보 삭제
        String key = RedisKeyManager.getChatRoomMemberKey(chatRoomId, memberId);
        String sessionId = headerAccessor.getSessionId();
        redisUtil.remove(key, sessionId);

        if (redisUtil.getAll(key).isEmpty()) {
            redisUtil.delete(key);
        }
    }
}