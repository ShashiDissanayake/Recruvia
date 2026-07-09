package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Company;
import com.recruvia.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByCompanyName(String companyName);

    boolean existsByCompanyName(String companyName);

    List<Company> findByRecruiter(User recruiter);

}