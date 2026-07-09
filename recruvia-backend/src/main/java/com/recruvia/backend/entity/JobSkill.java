package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "job_skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_job_skill",
                        columnNames = {"job_id", "skill_id"}
                )
        }
)
public class JobSkill extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @NotNull(message = "Job is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    @NotNull(message = "Skill is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Skill skill;

    @NotNull(message = "Required flag is mandatory.")
    @Column(nullable = false)
    @ToString.Include
    private Boolean required = true;

}