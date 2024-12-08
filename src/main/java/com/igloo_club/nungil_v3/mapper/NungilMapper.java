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
    @Mapping(source = "nungil.id", target = "nungilId")
    @Mapping(source = "nungil.opponent.nickname", target = "nickname")
    @Mapping(source = "nungil.opponent.birthdate", target = "birthdate")
    @Mapping(source = "nungil.opponent.company.companyName", target = "companyName")
    @Mapping(source = "nungil.opponent.profile.height", target = "height")
    @Mapping(source = "nungil.opponent.profile.religion", target = "religion")
    @Mapping(source = "nungil.opponent.profile.tattoo", target = "tattoo")
    @Mapping(source = "nungil.opponent.profile.smoke", target = "smoke")
    @Mapping(source = "nungil.opponent.profile.marriagePlan", target = "marriagePlan")
    @Mapping(source = "nungil.opponent.profile.mbtiType", target = "mbtiType")
    NungilResponse toResponse(Nungil nungil);

}
