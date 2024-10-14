package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nungil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private NungilStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id")
    private Member opponent;

    // == 정적 생성 메서드 == //
    public static Nungil create(Member member, Member opponent, NungilStatus status) {
        return Nungil.builder()
                .member(member)
                .opponent(opponent)
                .createdAt(LocalDateTime.now())
                .status(status)
                .build();
    }

    public void setStatus(NungilStatus status){
        this.status = status;
    }



}
