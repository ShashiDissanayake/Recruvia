package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Job;
import com.recruvia.backend.entity.JobSkill;
import com.recruvia.backend.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobSkillRepository extends JpaRepository<JobSkill, UUID> {

    List<JobSkill> findByJob(Job job);

    Optional<JobSkill> findByJobAndSkill(Job job, Skill skill);

}