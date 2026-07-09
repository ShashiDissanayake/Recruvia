package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import com.recruvia.backend.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_job_candidate",
                        columnNames = {"job_id", "user_id"}
                )
        }
)
public class Application extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @NotNull(message = "Job is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Candidate is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    @NotNull(message = "Resume is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Resume resume;

    @NotNull(message = "Application status is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    @ToString.Include
    private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

    @PositiveOrZero(message = "Match score cannot be negative.")
    @Column(name = "ai_match_score", precision = 5, scale = 2)
    @ToString.Include
    private BigDecimal aiMatchScore;

    @CreatedDate
    @Column(name = "applied_at", nullable = false, updatable = false)
    @ToString.Include
    private LocalDateTime appliedAt;

    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Interview> interviews = new ArrayList<>();
}