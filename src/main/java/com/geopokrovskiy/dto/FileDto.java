package com.geopokrovskiy.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.geopokrovskiy.entity.EventEntity;
import com.geopokrovskiy.entity.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileDto {
    @Id
    private Integer id;
    private String location;
    private Status status;
    private List<EventEntity> events;
}
