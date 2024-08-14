package com.igloo_club.nungil_v3.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyListResponse {

    private String domain;

    private List<String> companyNameList;

    private Integer totalCount;

    // == 생성 메서드 == //
    public static CompanyListResponse create(List<String> companyNameList, String domain) {
        return new CompanyListResponse(domain, companyNameList, companyNameList.size());
    }
}
