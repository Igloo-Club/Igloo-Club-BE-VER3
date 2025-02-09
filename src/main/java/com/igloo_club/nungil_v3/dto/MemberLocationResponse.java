package com.igloo_club.nungil_v3.dto;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.Location;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLocationResponse {
    Location location;

    public static MemberLocationResponse create(Member member) {
        MemberLocationResponse  memberLocationResponse= new MemberLocationResponse();
        memberLocationResponse.location = member.getLocation().get(0);

        return memberLocationResponse;
    }
}
