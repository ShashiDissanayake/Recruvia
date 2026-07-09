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

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "audit_logs")
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @NotBlank(message = "Action is required.")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    @ToString.Include
    private String action;

    @NotBlank(message = "Entity name is required.")
    @Size(max = 100)
    @Column(name = "entity_name", nullable = false, length = 100)
    @ToString.Include
    private String entityName;

    @Column(name = "entity_id")
    @ToString.Include
    private UUID entityId;

    @Size(max = 50)
    @Column(name = "ip_address", length = 50)
    @ToString.Include
    private String ipAddress;

}