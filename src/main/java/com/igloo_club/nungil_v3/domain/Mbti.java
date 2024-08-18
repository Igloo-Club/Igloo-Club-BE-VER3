package com.igloo_club.nungil_v3.domain;


import com.igloo_club.nungil_v3.domain.enums.MbtiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mbti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MbtiType mbtiType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ideal_id")
    private Ideal ideal;
}
