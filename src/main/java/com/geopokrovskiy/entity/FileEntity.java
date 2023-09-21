package com.geopokrovskiy.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
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
    @Column("location")
    private String location;
    @Column("status")
    private Status status;
    @Transient
    @ToString.Exclude
    private List<EventEntity> events;

}
