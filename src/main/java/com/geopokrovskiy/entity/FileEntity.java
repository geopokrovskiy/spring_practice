package com.geopokrovskiy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("files")
public class FileEntity {
    @Id
    private Integer id;
    private String location;
    private Status status;
    private List<EventEntity> events;

}
