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

    private String fcmToken;

    public Oauth update(String oauthAccessToken, String fcmToken) {
        this.oauthAccessToken = oauthAccessToken;
        this.fcmToken = fcmToken;

        return this;
    }

    public Oauth memberUpdate(Member member) {
        this.member = member;

        return this;
    }

    public void deleteFCMToken(){this.fcmToken = null;}
}
