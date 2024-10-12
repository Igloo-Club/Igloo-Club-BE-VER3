package com.igloo_club.nungil_v3.service;

import com.igloo_club.nungil_v3.domain.Ideal;
import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.MemberImage;
import com.igloo_club.nungil_v3.domain.Profile;
import com.igloo_club.nungil_v3.domain.enums.ImageStatus;
import com.igloo_club.nungil_v3.dto.*;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.IdealErrorResult;
import com.igloo_club.nungil_v3.exception.MemberErrorResult;
import com.igloo_club.nungil_v3.repository.IdealRepository;
import com.igloo_club.nungil_v3.repository.MemberImageRepository;
import com.igloo_club.nungil_v3.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final IdealRepository idealRepository;

    private final MemberImageRepository memberImageRepository;

    private final PresignedUrlService presignedUrlService;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorResult.USER_NOT_FOUND));
    }

    @Transactional
    public void createEssentialProfile(EssentialProfileCreateRequest request, Member member) {
        member.createEssentialProfile(request);
    }

    @Transactional
    public void createAdditionalProfile(AdditionalProfileCreateRequest request, Member member) {

        Profile profile = request.toProfile();
        member.createAdditionalProfile(profile);
    }

    @Transactional
    public void createLocation(LocationCreateRequest request, Member member) {
        member.addLocation(request.getLocation());
    }

    @Transactional
    public void createIdeal(IdealCreateRequest request, Member member) {

        Ideal ideal = request.toIdeal();
        member.createIdeal(ideal);
    }

    public IdealResponse getIdeal(Member member) {

        Ideal ideal = idealRepository.findById(member.getIdeal().getId())
                        .orElseThrow(()->new GeneralException(IdealErrorResult.IDEAL_NOT_FOUND));
        return IdealResponse.create(ideal);
    }

    public MemberProfileResponse getMemberProfile(Member member) {
        Profile profile = member.getProfile();
        List<String> imageUrlList = member.getMemberImageList().stream()
                .map(MemberImage::getFilename)
                .map(filename -> presignedUrlService.generatePresignedDownloadUrl(filename.toString()))
                .collect(Collectors.toList());

        return MemberProfileResponse.create(member, profile, imageUrlList);
    }

    @Transactional
    public MemberImageUploadUrlCreateResponse getImageUploadUrl(Member member) {
        Long count = memberImageRepository.countByMemberAndStatus(member, ImageStatus.UPLOAD_COMPLETE);
        if (count >= 3) {
            throw new GeneralException(MemberErrorResult.MEMBER_IMAGE_LIMIT);
        }

        // 새로 생성되는 파일명을 랜덤 UUID로 설정
        UUID filename = UUID.randomUUID();

        MemberImage memberImage = MemberImage.create(filename, ImageStatus.UPLOAD_IN_PROGRESS, member);
        member.addMemberImage(memberImage);

        String presignedUrl = presignedUrlService.generatePresignedUploadUrl(filename.toString());
        return MemberImageUploadUrlCreateResponse.create(presignedUrl, filename);
    }

    @Transactional
    public void notifyImageUpload(UUID filename, ImageStatus status, Member member) {

        MemberImage memberImage = memberImageRepository.findTopByMemberAndFilename(member, filename)
                .orElseThrow(() -> new GeneralException(MemberErrorResult.MEMBER_IMAGE_NOT_FOUND));

        if (status.equals(ImageStatus.UPLOAD_FAILED)) {
            memberImageRepository.delete(memberImage);
            member.getMemberImageList().remove(memberImage);
        }

        memberImage.updateStatusToComplete();
    }
}

