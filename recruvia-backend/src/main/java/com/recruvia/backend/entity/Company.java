package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "companies")
public class Company extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    @NotNull(message = "Recruiter is required.")
    private User recruiter;

    @NotBlank(message = "Company name is required.")
    @Size(max = 255, message = "Company name cannot exceed 255 characters.")
    @Column(name = "company_name", nullable = false, unique = true, length = 255)
    private String companyName;

    @Size(max = 5000, message = "Description is too long.")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 255, message = "Website URL cannot exceed 255 characters.")
    @Column(length = 255)
    private String website;

    @Size(max = 500, message = "Logo URL cannot exceed 500 characters.")
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Size(max = 100, message = "Industry cannot exceed 100 characters.")
    @Column(length = 100)
    private String industry;

    @Size(max = 255, message = "Location cannot exceed 255 characters.")
    @Column(length = 255)
    private String location;

    @Size(max = 50, message = "Company size cannot exceed 50 characters.")
    @Column(name = "company_size", length = 50)
    private String companySize;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Job> jobs = new ArrayList<>();

}