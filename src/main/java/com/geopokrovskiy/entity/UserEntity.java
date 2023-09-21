package com.geopokrovskiy.entity;

import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
@Builder(toBuilder = true)
public class UserEntity {
    @Id
    private Integer id;
    @Column("username")
    private String username;
    @Column("password")
    private String password;
    @Column("first_name")
    private String firstName;
    @Column("first_name")
    private String lastName;
    @Column("role")
    private UserRole role;
    @Column("status")
    private Status status;
    @Column("enabled")
    private boolean enabled;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Transient
    @ToString.Exclude
    private List<EventEntity> events;
    @ToString.Include(name = "password")
    private String maskPassword(){
        return "********";
    }

}
