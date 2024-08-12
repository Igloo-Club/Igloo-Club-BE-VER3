package com.igloo_club.nungil_v3.member;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import com.igloo_club.nungil_v3.domain.enums.Location;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import com.igloo_club.nungil_v3.dto.DetailedProfileCreateRequest;
import com.igloo_club.nungil_v3.dto.LocationCreateRequest;
import com.igloo_club.nungil_v3.dto.RequiredProfileCreateRequest;
import com.igloo_club.nungil_v3.repository.CompanyRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import com.igloo_club.nungil_v3.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        RequiredProfileCreateRequest request = new RequiredProfileCreateRequest("닉네임", Sex.MALE, LocalDate.of(2000, 10, 26));
        Member member = Member.builder().build();

        // when
        target.createRequiredProfile(request, member);

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
