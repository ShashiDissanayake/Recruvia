package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Application;
import com.recruvia.backend.entity.Job;
import com.recruvia.backend.entity.User;
import com.recruvia.backend.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findByUser(User user);

    List<Application> findByJob(Job job);

    Optional<Application> findByUserAndJob(User user, Job job);

    List<Application> findByStatus(ApplicationStatus status);

}