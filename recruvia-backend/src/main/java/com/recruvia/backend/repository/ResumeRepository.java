package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Resume;
import com.recruvia.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {

    List<Resume> findByUser(User user);

    Optional<Resume> findByUserAndIsActiveTrue(User user);

}