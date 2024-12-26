package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.QuestionAndAnswer;
import com.igloo_club.nungil_v3.domain.enums.Question;
import com.igloo_club.nungil_v3.domain.enums.QuestionCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionAndAnswerRepository extends JpaRepository<QuestionAndAnswer, Long> {
    @Query("SELECT qa.id " +
            "FROM QuestionAndAnswer qa " +
            "WHERE qa.member = :member " +
            "AND qa.exposureOrder BETWEEN :min AND :max " +
            "ORDER BY qa.exposureOrder ASC")
    Slice<Long> findIdsByMemberAndExposureOrderBetween(
            @Param("member") Member member,
            @Param("min") long min,
            @Param("max") long max,
            Pageable pageable
    );
    @Query("SELECT qa " +
            "FROM QuestionAndAnswer qa " +
            "JOIN FETCH qa.member m " +
            "WHERE qa.id IN :ids " +
            "ORDER BY FIELD(qa.id, :ids)")
    List<QuestionAndAnswer> findByIdsWithFetchJoin(@Param("ids") List<Long> ids);


    Optional<QuestionAndAnswer> findQuestionAndAnswerById(Long id);

    Optional<QuestionAndAnswer> findQuestionAndAnswerByMemberAndQuestion(Member member, Question question);

    Optional<QuestionAndAnswer> findByMemberAndExposureOrder(Member member, Long exposureOrder);

    List<QuestionAndAnswer> findAllByMemberAndQuestionCategory(Member member, QuestionCategory category);


}
