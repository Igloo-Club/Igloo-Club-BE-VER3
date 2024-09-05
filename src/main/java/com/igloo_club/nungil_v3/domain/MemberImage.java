package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.ImageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class MemberImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BINARY(16), nullable = false, updatable = false")
    private UUID filename;

    @Enumerated(value = EnumType.STRING)
    private ImageStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_image_id")
    private Member member;
}
