package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import com.recruvia.backend.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true, email = concat(email, '.deleted.', gen_random_uuid()) WHERE id = ?")
@SQLRestriction("deleted = false")
public class User extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "Role is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Role role;

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name cannot exceed 100 characters.")
    @Column(name = "first_name", nullable = false, length = 100)
    @ToString.Include
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name cannot exceed 100 characters.")
    @Column(name = "last_name", nullable = false, length = 100)
    @ToString.Include
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Size(max = 255, message = "Email cannot exceed 255 characters.")
    @Column(nullable = false, unique = true, length = 255)
    @ToString.Include
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters.")
    @Column(nullable = false, length = 255)
    private String password;

    @Pattern(
            regexp = "^\\+?\\d{10,15}$",
            message = "Phone number must contain 10 to 15 digits."
    )
    @Column(length = 20)
    @ToString.Include
    private String phone;

    @Size(max = 500, message = "Profile image URL cannot exceed 500 characters.")
    @Column(name = "profile_image", length = 500)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    @NotNull(message = "Account status is required.")
    @ToString.Include
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "recruiter", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Company> companies = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Resume> resumes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserSkill> userSkills = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Notification> notifications = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AuditLog> auditLogs = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private NotificationPreference notificationPreference;

}