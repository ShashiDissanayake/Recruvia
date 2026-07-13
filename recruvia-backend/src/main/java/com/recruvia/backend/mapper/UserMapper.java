package com.recruvia.backend.mapper;

import com.recruvia.backend.dto.user.UpdateProfileRequest;
import com.recruvia.backend.dto.user.UserResponse;
import com.recruvia.backend.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    @Mapping(target = "role", source = "role.name")
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "deleted", ignore = true)

    @Mapping(target = "companies", ignore = true)
    @Mapping(target = "resumes", ignore = true)
    @Mapping(target = "userSkills", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "auditLogs", ignore = true)
    @Mapping(target = "notificationPreference", ignore = true)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    void updateEntity(UpdateProfileRequest request, @MappingTarget User user);

}