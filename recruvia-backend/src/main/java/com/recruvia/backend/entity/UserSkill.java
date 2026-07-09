package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import com.recruvia.backend.enums.ProficiencyLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "user_skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_skill",
                        columnNames = {"user_id", "skill_id"}
                )
        }
)
public class UserSkill extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    @NotNull(message = "Skill is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Skill skill;

    @NotNull(message = "Proficiency level is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level")
    @ToString.Include
    private ProficiencyLevel proficiencyLevel;

    @PositiveOrZero(message = "Experience cannot be negative.")
    @Column(name = "years_of_experience", precision = 4, scale = 1)
    @ToString.Include
    private BigDecimal yearsOfExperience;

}