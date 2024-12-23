package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.ChatMessage;
import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom")
    Slice<ChatMessage> findByChatRoom(@Param("chatRoom") ChatRoom chatRoom, PageRequest pageRequest);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom ORDER BY cm.createdAt DESC")
    ChatMessage findTop1LastMessage(@Param("chatRoom") ChatRoom chatRoom, PageRequest pageRequest);

    Optional<ChatMessage> findTop1ByIdAndChatRoom(Long id, ChatRoom chatRoom);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom AND cm.member = :member AND cm.status = 'UNREAD'")
    List<ChatMessage> findUnreadByChatRoomAndMember(@Param("chatRoom") ChatRoom chatRoom, @Param("member") Member member);

    @Query("DELETE FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom")
    @Modifying
    void deleteAllByChatRoom(@Param("chatRoom") ChatRoom chatRoom);

    @Query("UPDATE ChatMessage cm SET cm.status = 'READ' WHERE cm.member = :member")
    @Modifying
    void updateStatusToReadByMember(@Param("member") Member member);
}
