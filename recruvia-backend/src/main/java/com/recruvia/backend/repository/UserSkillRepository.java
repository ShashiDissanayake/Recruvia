package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Skill;
import com.recruvia.backend.entity.User;
import com.recruvia.backend.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSkillRepository extends JpaRepository<UserSkill, UUID> {

    List<UserSkill> findByUser(User user);

    Optional<UserSkill> findByUserAndSkill(User user, Skill skill);

}