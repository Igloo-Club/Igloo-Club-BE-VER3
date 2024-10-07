package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockedMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id")
    private Member opponent;

    @Enumerated(EnumType.STRING)
    private NungilStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    private LocalDateTime expiredAt;

    // == 생성 메서드 == //
    public static BlockedMember create(Member member, Member opponent, NungilStatus status){
        BlockedMember blockedMember = new BlockedMember();

        blockedMember.member = member;
        blockedMember.opponent = opponent;
        blockedMember.status = status;
        blockedMember.createdAt = LocalDateTime.now();
        if(NungilStatus.RECEIVED.equals(status)){ // 수정 요망
            blockedMember.sentAt = LocalDateTime.now();
            blockedMember.expiredAt = blockedMember.sentAt.plusDays(7);
        }

        return blockedMember;
    }
}
