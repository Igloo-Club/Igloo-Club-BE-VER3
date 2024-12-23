package com.igloo_club.nungil_v3.mapper;

import com.igloo_club.nungil_v3.domain.Nungil;
import com.igloo_club.nungil_v3.dto.NungilResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NungilMapper {
    NungilMapper INSTANCE = Mappers.getMapper(NungilMapper.class);

    // Nungil 엔티티에서 NungilResponse로 매핑
    @Mapping(source = "nungil.member.id", target = "id")
    @Mapping(source = "nungil.member.nickname", target = "nickname")
    @Mapping(source = "nungil.member.birthdate", target = "birthdate")
    @Mapping(source = "nungil.member.company.companyName", target = "companyName")
    @Mapping(source = "nungil.member.profile.height", target = "height")
    @Mapping(source = "nungil.member.profile.religion", target = "religion")
    @Mapping(source = "nungil.member.profile.tattoo", target = "tattoo")
    @Mapping(source = "nungil.member.profile.smoke", target = "smoke")
    @Mapping(source = "nungil.member.profile.marriagePlan", target = "marriagePlan")
    @Mapping(source = "nungil.member.profile.mbtiType", target = "mbtiType")
    NungilResponse toResponse(Nungil nungil);

}
