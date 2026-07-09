package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import com.recruvia.backend.enums.EmploymentType;
import com.recruvia.backend.enums.JobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "jobs")
public class Job extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "Company is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Company company;

    @NotBlank(message = "Job title is required.")
    @Size(max = 255, message = "Job title cannot exceed 255 characters.")
    @Column(nullable = false, length = 255)
    @ToString.Include
    private String title;

    @NotBlank(message = "Job description is required.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Employment type is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    @ToString.Include
    private EmploymentType employmentType;

    @Size(max = 30, message = "Experience level cannot exceed 30 characters.")
    @Column(name = "experience_level", length = 30)
    @ToString.Include
    private String experienceLevel;

    @PositiveOrZero(message = "Minimum salary cannot be negative.")
    @Column(name = "salary_min", precision = 10, scale = 2)
    @ToString.Include
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "Maximum salary cannot be negative.")
    @Column(name = "salary_max", precision = 10, scale = 2)
    @ToString.Include
    private BigDecimal salaryMax;

    @Size(max = 255, message = "Location cannot exceed 255 characters.")
    @Column(length = 255)
    @ToString.Include
    private String location;

    @Column(name = "application_deadline")
    @ToString.Include
    private LocalDate applicationDeadline;

    @NotNull(message = "Job status is required.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ToString.Include
    private JobStatus status = JobStatus.OPEN;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<JobSkill> jobSkills = new ArrayList<>();

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Application> applications = new ArrayList<>();

}