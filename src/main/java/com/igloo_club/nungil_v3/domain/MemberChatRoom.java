package com.igloo_club.nungil_v3.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member_chat_room", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "chatroom_id"})
})
public class MemberChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    private boolean isDeleted = false;

    public static MemberChatRoom create(Member member, ChatRoom chatRoom) {
        MemberChatRoom memberChatRoom = new MemberChatRoom();

        member.addMemberChatRoom(memberChatRoom);
        chatRoom.addMemberChatRoom(memberChatRoom);

        return memberChatRoom;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public void setAsDeleted() {
        this.isDeleted = true;
    }

    public void removeMember() {
        this.member.getMemberChatRoomList().remove(this);
        this.member = null;
    }

}
