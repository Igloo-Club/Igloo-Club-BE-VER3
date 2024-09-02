package com.igloo_club.nungil_v3.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회사명 조회 응답")
public class CompanyListResponse {

    @Schema(description = "도메인 주소", example = "soongsil.ac.kr")
    private String domain;

    @ArraySchema(schema = @Schema(description = "회사명 리스트", example = "숭실대학교"))
    private List<String> companyNameList;

    @Schema(description = "회사명 개수", example = "1")
    private Integer totalCount;

    // == 생성 메서드 == //
    public static CompanyListResponse create(List<String> companyNameList, String domain) {
        return new CompanyListResponse(domain, companyNameList, companyNameList.size());
    }
}
