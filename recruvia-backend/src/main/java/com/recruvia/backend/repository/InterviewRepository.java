package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Application;
import com.recruvia.backend.entity.Interview;
import com.recruvia.backend.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    List<Interview> findByApplication(Application application);

    List<Interview> findByInterviewStatus(InterviewStatus status);

}