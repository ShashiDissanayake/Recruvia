package com.recruvia.backend.repository;

import com.recruvia.backend.entity.NotificationPreference;
import com.recruvia.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, UUID> {

    Optional<NotificationPreference> findByUser(User user);

}
