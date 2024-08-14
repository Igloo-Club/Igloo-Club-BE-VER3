package com.igloo_club.nungil_v3.member;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import com.igloo_club.nungil_v3.dto.LocationCreateRequest;
import com.igloo_club.nungil_v3.dto.EssentialProfileCreateRequest;
import com.igloo_club.nungil_v3.repository.CompanyRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import com.igloo_club.nungil_v3.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService target;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void 필수프로필등록성공() {
        // given
        EssentialProfileCreateRequest request = new EssentialProfileCreateRequest("닉네임", Sex.MALE, LocalDate.of(2000, 10, 26));
        Member member = Member.builder().build();

        // when
        target.createEssentialProfile(request, member);

        // then
        assertThat(member.getNickname()).isEqualTo("닉네임");
        assertThat(member.getSex()).isEqualTo(Sex.MALE);
        assertThat(member.getBirthdate()).isEqualTo(LocalDate.of(2000, 10, 26));
    }
    @Test
    public void 근무지등록성공() {
        // given
        Location location = Location.PANGYO;

        LocationCreateRequest request = new LocationCreateRequest(location);
        Member member = Member.builder().build();

        // when
        target.createLocation(request, member);

        // then
        assertThat(member.getLocation().size()).isEqualTo(1);
        assertThat(member.getLocation().get(0)).isEqualTo(location);
    }
}
