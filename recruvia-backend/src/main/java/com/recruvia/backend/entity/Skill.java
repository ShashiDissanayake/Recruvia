package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "skills")
public class Skill extends BaseEntity {

    @NotBlank(message = "Skill name is required.")
    @Size(max = 100, message = "Skill name cannot exceed 100 characters.")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Size(max = 100, message = "Category cannot exceed 100 characters.")
    @Column(length = 100)
    private String category;

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private List<UserSkill> userSkills = new ArrayList<>();

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private List<JobSkill> jobSkills = new ArrayList<>();
}