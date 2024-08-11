package com.igloo_club.nungil_v3.member;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import com.igloo_club.nungil_v3.domain.enums.Sex;
import com.igloo_club.nungil_v3.dto.DetailedProfileCreateRequest;
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
    public void 상세프로필등록성공_회사규모파라미터가NULL() {
        // given
        DetailedProfileCreateRequest request = new DetailedProfileCreateRequest();

        Company company = Company.builder().scale(CompanyScale.LARGE).build();
        Member member = Member.builder().company(company).build();

        // when
        target.createDetailedProfile(request, member);

        // then
        assertThat(request.getScale()).isNull();
        assertThat(member.getCompany().getScale()).isEqualTo(CompanyScale.LARGE);
    }

    @ParameterizedTest
    @MethodSource("detailedProfileCreate_sameScale")
    public void 상세프로필등록성공_회사규모파라미터가동일(CompanyScale requestScale, CompanyScale companyScale) throws NoSuchFieldException, IllegalAccessException {
        // given
        doReturn(Company.builder().build()).when(companyRepository).save(any(Company.class));

        DetailedProfileCreateRequest request = new DetailedProfileCreateRequest();
        Class clazz = request.getClass();

        // Request DTO의 scale 필드와, 회원의 현재 회사 scale을 동일하게 지정
        Field field = clazz.getDeclaredField("scale");
        field.setAccessible(true);
        field.set(request, requestScale);

        Company company = Company.builder().scale(companyScale).build();
        Member member = Member.builder().company(company).build();

        // when
        target.createDetailedProfile(request, member);

        // then
        assertThat(requestScale).isEqualTo(companyScale);
        assertThat(member.getCompany() == company).isTrue();
        assertThat(member.getCompany().getScale()).isEqualTo(requestScale);
    }

    private static Stream<Arguments> detailedProfileCreate_sameScale() {
        Stream.Builder<Arguments> builder = Stream.builder();
        Arrays.stream(CompanyScale.values())
                .forEach(scale -> builder.add(Arguments.of(scale, scale)));
        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("detailedProfileCreate_differentScale")
    public void 상세프로필등록성공_회사규모파라미터가상이(CompanyScale requestScale, CompanyScale companyScale) throws NoSuchFieldException, IllegalAccessException {
        // given
        doReturn(Company.builder().build()).when(companyRepository).save(any(Company.class));

        DetailedProfileCreateRequest request = new DetailedProfileCreateRequest();
        Class clazz = request.getClass();

        // Request DTO의 scale 필드와, 회원의 현재 회사 scale을 서로 상이하게 지정
        Field field = clazz.getDeclaredField("scale");
        field.setAccessible(true);
        field.set(request, requestScale);

        Company company = Company.builder().scale(companyScale).build();
        Member member = Member.builder().company(company).build();

        // when
        target.createDetailedProfile(request, member);

        // then
        assertThat(requestScale).isNotEqualTo(companyScale);
        assertThat(member.getCompany() == company).isFalse();
        assertThat(member.getCompany().getScale()).isEqualTo(requestScale);
        assertThat(member.getCompany().getScale()).isNotEqualTo(companyScale);
    }

    private static Stream<Arguments> detailedProfileCreate_differentScale() {
        Stream.Builder<Arguments> builder = Stream.builder();
        CompanyScale[] scales = CompanyScale.values();

        Arrays.stream(scales)
                .forEach(scale1 -> Arrays.stream(scales)
                        .filter(scale2 -> !scale1.equals(scale2))
                        .forEach(scale2 -> builder.add(Arguments.of(scale1, scale2)))
                );

        return builder.build();
    }
}
