package com.geopokrovskiy.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.geopokrovskiy.entity.EventString;
import com.geopokrovskiy.entity.FileEntity;
import com.geopokrovskiy.entity.Status;
import com.geopokrovskiy.entity.UserEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDto {
    @Id
    private Integer id;
    private UserEntity userEntity;
    private FileEntity fileEntity;
    private Status status;
    private EventString eventString;

}
