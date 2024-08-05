package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.OauthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Oauth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    private String oauthAccessToken;

    private String oauthId;

    public Oauth update(String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;

        return this;
    }

    public Oauth memberUpdate(Member member) {
        this.member = member;

        return this;
    }
}
