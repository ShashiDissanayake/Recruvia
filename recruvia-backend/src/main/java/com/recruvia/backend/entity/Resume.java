package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Table(name = "resumes")
public class Resume extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @NotBlank(message = "File name is required.")
    @Size(max = 255)
    @Column(name = "file_name", nullable = false, length = 255)
    @ToString.Include
    private String fileName;

    @NotBlank(message = "File URL is required.")
    @Size(max = 500)
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @NotBlank(message = "File type is required.")
    @Size(max = 20)
    @Column(name = "file_type", nullable = false, length = 20)
    @ToString.Include
    private String fileType;

    @NotNull(message = "File size is required.")
    @Positive(message = "File size must be greater than zero.")
    @Column(name = "file_size", nullable = false)
    @ToString.Include
    private Long fileSize;

    @Size(max = 5000)
    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "ai_processed", nullable = false)
    @ToString.Include
    private Boolean aiProcessed = false;

    @Column(name = "is_active", nullable = false)
    @ToString.Include
    private Boolean active = true;

    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Application> applications = new ArrayList<>();

}