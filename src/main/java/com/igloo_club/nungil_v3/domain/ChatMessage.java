package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.ChatMessageStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 400)
    @Getter(AccessLevel.NONE)
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChatMessageStatus status = ChatMessageStatus.UNREAD;

    private LocalDateTime createdAt;

    public String getContent() {
        if (ChatMessageStatus.DELETED.equals(this.status)) {
            return "삭제된 메시지입니다.";
        }
        return this.content;
    }

    public static ChatMessage create(ChatRoom chatRoom, Member member, String content) {
        ChatMessage chatMessage = new ChatMessage();
        LocalDateTime now = LocalDateTime.now();

        chatMessage.chatRoom = chatRoom;
        chatMessage.member = member;
        chatMessage.content = content;
        chatMessage.createdAt = now;

        chatRoom.getChatMessageList().add(chatMessage);
        chatRoom.setLastMessageAtToNow(now);

        return chatMessage;
    }

    public void updateAsDeleted() {
        this.status = ChatMessageStatus.DELETED;
    }

    public void setStatusAsRead() {
        this.status = ChatMessageStatus.READ;
    }
}
