package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.Nungil;
import com.igloo_club.nungil_v3.domain.enums.NungilStatus;
import com.igloo_club.nungil_v3.dto.NungilDetailResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NungilRepository extends JpaRepository<Nungil, Long> {
    Slice<Nungil> findAllByMemberAndStatus(PageRequest request, Member member, NungilStatus status);
    List<Nungil> findAllByMemberAndOpponentAndStatus(Member member, Member receiver, NungilStatus status);
    @Query("SELECT new com.igloo_club.nungil_v3.dto.NungilDetailResponse(" +
            "n.id, n.status, m.nickname, m.birthdate, p.intro, c.companyName, m.job, c.scale, " +
            "m.location, p.height, p.religion, p.tattoo, p.smoke, p.marriagePlan, p.mbtiType, " +
            "(SELECT COUNT(qa) FROM QuestionAndAnswer qa WHERE qa.member.id = m.id AND qa.exposureOrder IS NOT NULL), " +
            "(SELECT qa FROM QuestionAndAnswer qa WHERE qa.member.id = m.id AND qa.exposing = true)) " +
            "FROM Nungil n " +
            "JOIN n.opponent m " +
            "JOIN m.profile p " +
            "JOIN m.company c " +
            "LEFT JOIN p.hobbyList h " +
            "WHERE n.id = :nungilId " +
            "GROUP BY n.id, m.id, p.id, c.id")
    Optional<NungilDetailResponse> findFullNungilDetailNative(@Param("nungilId") Long nungilId);

}
