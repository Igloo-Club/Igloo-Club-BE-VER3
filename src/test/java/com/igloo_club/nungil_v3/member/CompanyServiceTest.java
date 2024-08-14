package com.igloo_club.nungil_v3.member;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import com.igloo_club.nungil_v3.repository.CompanyRepository;
import com.igloo_club.nungil_v3.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService target;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void 회사규모등록성공_회사규모파라미터가NULL() {
        // given
        CompanyScale scale = CompanyScale.LARGE;
        Company company = getCompanyWithScale(scale);
        Member member = Member.builder().company(company).build();

        // when
        target.registerCompanyScale(null, company, member);

        // then
        assertThat(member.getCompany().getScale()).isEqualTo(scale);
    }

    private Company getCompanyWithScale(CompanyScale scale) {
        return Company.builder().companyName("회사명").email("email.com").scale(scale).build();
    }

    @ParameterizedTest
    @MethodSource("detailedProfileCreate_sameScale")
    public void 회사규모등록성공_회사규모파라미터가동일(CompanyScale requestScale, CompanyScale companyScale) {
        // given
        Company company = getCompanyWithScale(companyScale);
        Member member = Member.builder().company(company).build();

        // when
        target.registerCompanyScale(requestScale, company, member);

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
    public void 회사규모등록성공_회사규모파라미터가상이(CompanyScale requestScale, CompanyScale companyScale) {
        // given
        doReturn(Optional.of(getCompanyWithScale(requestScale))).when(companyRepository)
                .findByCompanyNameAndEmailAndScale(anyString(), anyString(), eq(requestScale));


        Company company = getCompanyWithScale(companyScale);
        Member member = Member.builder().company(company).build();

        // when
        target.registerCompanyScale(requestScale, company, member);

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
