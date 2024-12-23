package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.ChatRoom;
import com.igloo_club.nungil_v3.domain.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, PagingAndSortingRepository<ChatRoom, Long> {

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "JOIN cr.memberChatRoomList mcr " +
            "WHERE (cr.sender = :member OR cr.receiver = :member) AND mcr.isDeleted = false")
    Slice<ChatRoom> findActiveChatRoomByMember(@Param("member") Member member, Pageable pageable);

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.sender = :member1 AND cr.receiver = :member2) " +
            "OR (cr.sender = :member2 and cr.receiver = :member1)")
    Optional<ChatRoom> findChatRoomBetweenMembers(@Param("member1") Member member1, @Param("member2") Member member2);
}
