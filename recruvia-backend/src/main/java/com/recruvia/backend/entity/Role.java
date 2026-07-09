package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "roles")
public class Role extends BaseEntity {

    @NotBlank(message = "Role name is required.")
    @Size(max = 50, message = "Role name cannot exceed 50 characters.")
    @Column(nullable = false, unique = true, length = 50)
    @ToString.Include
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters.")
    @Column(length = 255)
    @ToString.Include
    private String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();

}