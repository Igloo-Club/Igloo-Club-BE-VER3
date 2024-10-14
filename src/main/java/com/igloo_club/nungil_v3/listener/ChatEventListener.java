package com.igloo_club.nungil_v3.listener;

import com.igloo_club.nungil_v3.domain.ChatDTO;
import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.ChatMessageStatus;
import com.igloo_club.nungil_v3.exception.ChatRoomErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.repository.ChatMessageRepository;
import com.igloo_club.nungil_v3.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class ChatEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    @EventListener
    @Transactional
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String destination = headerAccessor.getDestination();
        if (destination == null || !destination.startsWith("/topic/chatroom/")) {
            return;
        }

        Principal principal = headerAccessor.getUser();
        if (principal == null) {
            return;
        }

        Long memberId = Long.parseLong(principal.getName()); // 입장한 사용자 이름 추출
        Long chatRoomId = Long.parseLong(destination.split("/topic/chatroom/")[1]); // 채팅방 ID 추출

        // 1. 상대방이 보낸 "UNREAD" 상태의 메시지를 전부 "READ"로 변경
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ChatRoomErrorResult.CHAT_ROOM_NOT_FOUND));
        Member opponent = chatRoom.getOpponent(memberId);
        chatMessageRepository.updateStatusToReadByMember(opponent);

        // 2. 클라이언트에게 "READ" 상태로 변경됨을 알림
        ChatDTO chatDTO = ChatDTO.of(chatRoomId, opponent, ChatMessageStatus.ALL_READ);
        messagingTemplate.convertAndSend(destination, chatDTO);
    }
}