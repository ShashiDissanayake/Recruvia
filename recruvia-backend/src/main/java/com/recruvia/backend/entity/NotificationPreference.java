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
@Table(name = "notification_preferences")
public class NotificationPreference extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull(message = "User is required.")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "email_notifications", nullable = false)
    @NotNull
    private Boolean emailNotifications = true;

    @Column(name = "interview_notifications", nullable = false)
    @NotNull
    private Boolean interviewNotifications = true;

    @Column(name = "job_recommendation_notifications", nullable = false)
    @NotNull
    private Boolean jobRecommendationNotifications = true;

    @Column(name = "marketing_emails", nullable = false)
    @NotNull
    private Boolean marketingEmails = false;

}
