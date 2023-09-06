package com.geopokrovskiy.entity;

import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
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
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
    private Status status;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<EventEntity> events;
    @ToString.Include(name = "password")
    private String maskPassword(){
        return "********";
    }

}
