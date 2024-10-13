package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, PagingAndSortingRepository<ChatRoom, Long> {

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "JOIN cr.memberChatRoomList mcr " +
            "WHERE (cr.sender = :sender OR cr.receiver = :receiver) AND mcr.isDeleted = false")
    Slice<ChatRoom> findActiveChatRoomByMember(@Param("sender") Member sender, @Param("receiver") Member receiver, Pageable pageable);
}
