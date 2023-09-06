package com.geopokrovskiy.mapper;

import com.geopokrovskiy.dto.UserDto;
import com.geopokrovskiy.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDto userDto);

}
