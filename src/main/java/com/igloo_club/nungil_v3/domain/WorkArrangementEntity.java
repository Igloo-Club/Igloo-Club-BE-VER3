package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.WorkArrangement;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "work_arrangement")
public class WorkArrangementEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(value = EnumType.STRING)
    private WorkArrangement workArrangement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
