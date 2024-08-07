package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Company;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.CompanyListResponse;
import com.igloo_club.nungil_v3.dto.CompanyRegisterRequest;
import com.igloo_club.nungil_v3.dto.CompanyVerificationRequest;
import com.igloo_club.nungil_v3.exception.CompanyErrorResult;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.GlobalErrorResult;
import com.igloo_club.nungil_v3.exception.MemberErrorResult;
import com.igloo_club.nungil_v3.repository.CompanyRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import com.igloo_club.nungil_v3.util.EmailMessage;
import com.igloo_club.nungil_v3.util.EmailSender;
import com.igloo_club.nungil_v3.util.RandomStringUtil;
import com.igloo_club.nungil_v3.util.StringRedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private static final List<String> unavailableDomainList = List.of("naver.com", "gmail.com", "daum.net", "kakao.com", "yahoo.com", "hanmail.net");

    private final StringRedisUtil redisUtil;

    private final EmailSender emailSender;

    private final MemberRepository memberRepository;

    private final CompanyRepository companyRepository;

    public void sendAuthEmail(String email, Member member) {

        // 1. 사용이 불가능한 도메인인지 확인한다.
        validateDomain(extractDomain(email));

        // 2. 이미 가입된 이메일인지 확인한다.
        checkDuplicatedEmail(email, member);

        // 3. 인증번호를 발급한다.
        String code = RandomStringUtil.numeric(6);

        // 4. redis에 이메일을 키로 하여 인증번호를 저장한다.
        if (redisUtil.exists(email)) {
            redisUtil.delete(email);
        }
        redisUtil.set(email, code, Duration.ofMinutes(5));

        // 5. 이메일을 발송한다.
        String subject = "[눈길] 회사 인증 메일입니다.";
        String filename = "company-authentication.html";

        emailSender.send(EmailMessage.create(email, subject, filename).addContext("code", code));
    }

    /**
     * 인증번호를 검증하고, 성공한 경우 회원 정보의 회사 정보를 수정하는 메서드이다.
     * @param request 인증번호 검증 요청 DTO
     * @param member 검증을 요청한 회원 엔티티
     */
    @Transactional
    public void verifyAuthCode(CompanyVerificationRequest request, Member member) {
        String email = request.getEmail();
        String companyDomain = extractDomain(email);

        // 1. 사용이 불가능한 회사 도메인인지 확인한다.
        validateDomain(companyDomain);

        // 2. 이미 가입된 이메일인지 확인한다.
        checkDuplicatedEmail(email, member);

        // 3. 인증번호가 올바른지 검증한다.
        checkAuthCode(request.getCode(), email);
    }

    public CompanyListResponse getCompanyList(String email) {

        // 1. 사용이 불가능한 회사 도메인이면 탐색을 막는다.
        String domain = extractDomain(email);
        validateDomain(domain);

        // 2. 최대 3개의 회사명을 오름차순으로 정렬하여 조회
        List<Company> companyList = companyRepository.findTop3ByEmailOrderByCompanyNameAsc(domain);
        List<String> companyNameList = companyList.stream()
                .map(Company::getCompanyName)
                .distinct()
                .collect(Collectors.toList());

        return CompanyListResponse.create(companyNameList, domain);
    }

    @Transactional
    public void registerCompany(CompanyRegisterRequest request, Member member) {
        String email = request.getEmail();
        String companyName = request.getCompanyName();
        String domain = extractDomain(email);

        // 1. 사용이 불가능한 회사 도메인인지 확인한다.
        validateDomain(domain);

        // 2. 이미 가입된 이메일인지 확인한다.
        checkDuplicatedEmail(email, member);

        // 3. 주어진 회사명과 도메인을 가진 Company 엔티티를 조회한다.
        Company company = getCompany(companyName, domain);

        // 4. 회원 정보에 이메일과 회사를 등록한다.
        member.setCompanyAndEmail(company, email);
    }

    /**
     * 주어진 회사명과 도메인을 가진 Company 엔티티가 존재하면 조회하고,
     * 그렇지 않으면 생성하여 반환하는 메서드이다.
     *
     * @param companyName 회사명
     * @param domain 회사 도메인
     * @return Company 엔티티
     */
    private Company getCompany(String companyName, String domain) {
        Company company = companyRepository.findByCompanyNameAndEmail(companyName, domain)
                .orElse(Company.builder()
                        .companyName(companyName)
                        .email(domain)
                        .build());
        companyRepository.save(company);
        return company;
    }

    private void checkAuthCode(String code, String email) throws GeneralException {
        // 1. 요청된 회사 이메일을 키로 갖는 인증번호가 없거나 만료된 경우
        String foundCode = redisUtil.get(email);
        if (foundCode == null) {
            throw new GeneralException(GlobalErrorResult.REDIS_NOT_FOUND);
        }

        // 2. 주어진 인증번호가 틀린 경우
        if (!foundCode.equals(code)) {
            throw new GeneralException(CompanyErrorResult.WRONG_AUTH_CODE);
        }
    }

    /**
     * 주어진 이메일 주소에서 도메인을 추출하여 반환하는 메서드이다.
     * @param email 이메일 주소
     * @return 도메인
     */
    private String extractDomain(String email) {
        return email.substring(1 + email.indexOf('@'));
    }

    /**
     * 사용이 불가능한 도메인인 경우, 예외를 발생시키는 메서드이다.
     * @param domain 검사할 도메인
     */
    private void validateDomain(String domain) {
        if (unavailableDomainList.contains(domain)) {
            throw new GeneralException(CompanyErrorResult.UNAVAILABLE_EMAIL);
        }
    }

    /**
     * 주어진 email을 사용하는 회원이 존재하는지 확인하고, 다른 사람이 이미 사용한다면 예외를 발생시키는 메서드이다.
     * @param email 회원 이메일
     * @param requester 이메일 전송 요청자
     */
    private void checkDuplicatedEmail(String email, Member requester) {
        Optional<Member> optional = memberRepository.findByEmail(email);
        // 주어진 email을 사용하는 회원이 존재하지 않으면 종료
        if (optional.isEmpty()) {
            return;
        }

        // 주어진 email을 사용하는 회원이 자기자신이면 종료
        Member member = optional.get();
        if (member.getId().equals(requester.getId())) {
            return;
        }

        throw new GeneralException(MemberErrorResult.DUPLICATED_EMAIL);
    }
}
