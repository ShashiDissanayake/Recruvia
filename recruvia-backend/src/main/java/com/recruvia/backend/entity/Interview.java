package com.recruvia.backend.entity;

import com.recruvia.backend.entity.base.BaseEntity;
import com.recruvia.backend.enums.InterviewStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "interviews")
public class Interview extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "application_id", nullable = false, unique = true)
    @NotNull(message = "Application is required.")
    private Application application;

    @Size(max = 500)
    @Column(name = "meeting_link", length = 500)
    private String meetingLink;

    @NotNull(message = "Interview schedule is required.")
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Interview status is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status", nullable = false)
    private InterviewStatus interviewStatus = InterviewStatus.SCHEDULED;

    @Size(max = 5000)
    @Column(columnDefinition = "TEXT")
    private String notes;
}