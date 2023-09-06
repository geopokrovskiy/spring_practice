package com.geopokrovskiy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("events")
public class EventEntity {
    @Id
    private Integer id;
    @Column("user_id")
    private UserEntity userEntity;
    @Column("file_id")
    private FileEntity fileEntity;
    private Status status;
    private EventString eventString;
}
