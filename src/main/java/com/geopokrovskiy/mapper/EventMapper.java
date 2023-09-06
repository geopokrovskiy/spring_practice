package com.geopokrovskiy.mapper;

import com.geopokrovskiy.dto.EventDto;
import com.geopokrovskiy.entity.EventEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDto map(EventEntity eventEntity);

    @InheritInverseConfiguration
    EventEntity map(EventDto eventDto);
}
