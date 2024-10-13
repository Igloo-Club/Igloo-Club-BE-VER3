package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.ChatMessage;
import com.igloo_club.nungil_v3.domain.ChatRoom;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Slice<ChatMessage> findByChatRoom(PageRequest pageRequest, ChatRoom chatRoom);

    ChatMessage findTop1ByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

    Optional<ChatMessage> findTop1ByIdAndChatRoom(Long id, ChatRoom chatRoom);
}
