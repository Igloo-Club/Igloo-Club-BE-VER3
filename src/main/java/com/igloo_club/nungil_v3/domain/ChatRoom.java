package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.exception.ChatRoomErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
})
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChatMessage> chatMessageList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<MemberChatRoom> memberChatRoomList = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime lastMessageAt;

    // == 생성 메서드 == //

    /**
     * 채팅방 생성이 필요할 시, ChatMessageService.createChatRoom() 메서드를 이용하세요.
     */
    public static ChatRoom create(Member receiver, Member sender) {
        LocalDateTime now = LocalDateTime.now();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.sender = sender;
        chatRoom.receiver = receiver;
        chatRoom.createdAt = now;
        chatRoom.lastMessageAt = now;   // 채팅방 목록 조회 시에 새로 생성된 채팅방이 최상단에 위치하도록 하기 위함

        return chatRoom;
    }

    // == 비즈니스 로직 == //
    public void setLastMessageAtToNow(LocalDateTime now) {
        this.lastMessageAt = now;
    }

    public boolean isInactiveChatRoom() {
        return this.memberChatRoomList.stream()
                .anyMatch(MemberChatRoom::isDeleted);
    }

    public void addMemberChatRoom(MemberChatRoom memberChatRoom) {
        this.memberChatRoomList.add(memberChatRoom);
        memberChatRoom.setChatRoom(this);
    }

    public boolean isAllDeleted() {
        return this.memberChatRoomList.stream()
                .allMatch(MemberChatRoom::isDeleted);
    }

    public Member getOpponent(Member member) {
        if (member.equals(this.sender)) {
            return this.receiver;
        } else if (member.equals(this.receiver)) {
            return this.sender;
        } else {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }
    }

    public Member getOpponent(Long memberId) {
        if (memberId.equals(this.sender.getId())) {
            return this.receiver;
        } else if (memberId.equals(this.receiver.getId())) {
            return this.sender;
        } else {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }
    }
}
