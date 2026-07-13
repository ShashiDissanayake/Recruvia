package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Application;
import com.recruvia.backend.entity.Interview;
import com.recruvia.backend.entity.User;
import com.recruvia.backend.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    List<Interview> findByApplication(Application application);

    List<Interview> findByInterviewStatus(InterviewStatus status);

    @Query("SELECT COUNT(i) FROM Interview i WHERE i.application.user = :user AND i.interviewStatus = com.recruvia.backend.enums.InterviewStatus.SCHEDULED")
    long countScheduledInterviewsByUser(@Param("user") User user);

}