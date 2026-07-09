package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "skills")
public class Skill extends BaseEntity {

    @NotBlank(message = "Skill name is required.")
    @Size(max = 100, message = "Skill name cannot exceed 100 characters.")
    @Column(nullable = false, unique = true, length = 100)
    @ToString.Include
    private String name;

    @Size(max = 100, message = "Category cannot exceed 100 characters.")
    @Column(length = 100)
    @ToString.Include
    private String category;

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserSkill> userSkills = new ArrayList<>();

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<JobSkill> jobSkills = new ArrayList<>();
}