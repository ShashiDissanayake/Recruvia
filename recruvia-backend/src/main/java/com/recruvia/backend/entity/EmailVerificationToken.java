package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "email_verification_tokens")
public class EmailVerificationToken extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull(message = "User is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @NotBlank(message = "Token is required.")
    @Size(max = 500)
    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @NotNull(message = "Expiration date is required.")
    @Column(name = "expires_at", nullable = false)
    @ToString.Include
    private LocalDateTime expiresAt;

}
