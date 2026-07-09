package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import com.recruvia.backend.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @NotBlank(message = "Title is required.")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    @ToString.Include
    private String title;

    @NotBlank(message = "Message is required.")
    @Size(max = 5000)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @NotNull(message = "Notification type is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    @ToString.Include
    private NotificationType notificationType;

    @Column(name = "is_read", nullable = false)
    @ToString.Include
    private Boolean isRead = false;

}