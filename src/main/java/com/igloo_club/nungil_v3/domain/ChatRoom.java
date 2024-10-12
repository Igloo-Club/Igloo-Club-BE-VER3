package com.igloo_club.nungil_v3.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChatMessage> chatMessageList = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime lastMessageAt;

    // == 생성 메서드 == //
    public static ChatRoom create(Member receiver, Member sender) {
        LocalDateTime now = LocalDateTime.now();

        return ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .createdAt(now)
                .lastMessageAt(now) // 채팅방 목록 조회 시에 새로 생성된 채팅방이 최상단에 위치하도록 하기 위함
                .build();
    }

    // == 비즈니스 로직 == //
    public void setLastMessageAtToNow(LocalDateTime now) {
        this.lastMessageAt = now;
    }
}
