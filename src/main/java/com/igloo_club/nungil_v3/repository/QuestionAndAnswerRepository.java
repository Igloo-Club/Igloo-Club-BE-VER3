package com.igloo_club.nungil_v3.repository;

import com.igloo_club.nungil_v3.domain.Member;
import com.igloo_club.nungil_v3.domain.QuestionAndAnswer;
import com.igloo_club.nungil_v3.domain.enums.Question;
import com.igloo_club.nungil_v3.domain.enums.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionAndAnswerRepository extends JpaRepository<QuestionAndAnswer, Long> {
    Slice<QuestionAndAnswer> findAllByMemberAndExposureOrderBetween(Member member, Long start, Long end);

    Optional<QuestionAndAnswer> findQuestionAndAnswerById(Long id);

    Optional<QuestionAndAnswer> findQuestionAndAnswerByMemberAndQuestion(Member member, Question question);

    Optional<QuestionAndAnswer> findByMemberAndExposureOrder(Member member, Long exposureOrder);

    List<QuestionAndAnswer> findAllByMemberAndQuestionCategory(Member member, QuestionCategory category);


}
