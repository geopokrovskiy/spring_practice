package com.geopokrovskiy.mapper;

import com.geopokrovskiy.dto.FileDto;
import com.geopokrovskiy.entity.FileEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDto map(FileEntity fileEntity);
    @InheritInverseConfiguration
    FileEntity map(FileDto fileDto);
}
