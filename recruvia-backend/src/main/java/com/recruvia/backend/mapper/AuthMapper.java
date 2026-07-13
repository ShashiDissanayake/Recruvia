package com.recruvia.backend.mapper;

import com.recruvia.backend.dto.auth.RegisterRequest;
import com.recruvia.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "profileImage", ignore = true)

    @Mapping(target = "companies", ignore = true)
    @Mapping(target = "resumes", ignore = true)
    @Mapping(target = "userSkills", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "auditLogs", ignore = true)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "notificationPreference", ignore = true)

    User toEntity(RegisterRequest request);

}