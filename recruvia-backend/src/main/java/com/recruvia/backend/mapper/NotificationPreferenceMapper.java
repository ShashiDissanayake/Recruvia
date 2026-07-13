package com.recruvia.backend.mapper;

import com.recruvia.backend.dto.user.NotificationPreferenceResponse;
import com.recruvia.backend.dto.user.UpdateNotificationPreferenceRequest;
import com.recruvia.backend.entity.NotificationPreference;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfiguration.class)
public interface NotificationPreferenceMapper {

    NotificationPreferenceResponse toResponse(NotificationPreference preference);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateNotificationPreferenceRequest request, @MappingTarget NotificationPreference preference);

}
