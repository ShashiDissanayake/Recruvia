package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Company;
import com.recruvia.backend.entity.Job;
import com.recruvia.backend.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findByCompany(Company company);

    List<Job> findByStatus(JobStatus status);

    List<Job> findByCompanyAndStatus(Company company, JobStatus status);

}