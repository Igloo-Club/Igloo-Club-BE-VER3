package com.igloo_club.nungil_v3.mapper;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    // Member 엔티티에서 NungilResponse로 매핑
    @Mapping(source = "member.id", target = "id")
    @Mapping(source = "member.nickname", target = "nickname")
    @Mapping(source = "member.birthdate", target = "birthdate")
    @Mapping(source = "member.company.companyName", target = "companyName")
    @Mapping(source = "member.profile.height", target = "height")
    @Mapping(source = "member.profile.religion", target = "religion")
    @Mapping(source = "member.profile.tattoo", target = "tattoo")
    @Mapping(source = "member.profile.smoke", target = "smoke")
    @Mapping(source = "member.profile.marriagePlan", target = "marriagePlan")
    @Mapping(source = "member.profile.mbtiType", target = "mbtiType")
    NungilResponse toResponse(Member member);

}
