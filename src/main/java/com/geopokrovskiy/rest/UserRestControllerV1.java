package com.geopokrovskiy.rest;

import com.geopokrovskiy.dto.UserDto;
import com.geopokrovskiy.entity.Status;
import com.geopokrovskiy.entity.UserEntity;
import com.geopokrovskiy.entity.UserRole;
import com.geopokrovskiy.exception.UnauthorizedException;
import com.geopokrovskiy.mapper.UserMapper;
import com.geopokrovskiy.security.SecurityService;
import com.geopokrovskiy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserRestControllerV1 {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserDto> getAllUsers(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()))) {
            return userService.getAllUsers()
                    .map(userMapper::map);
        } else {
            return Flux.error(new UnauthorizedException("Access denied!"));
        }
    }

    @GetMapping("/active_users")
    public Flux<UserDto> getAllActiveUsers(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.MODERATOR.toString()))) {
            return userService.getAllUsers().filter(u -> u.getStatus() == Status.ACTIVE).map(userMapper::map);
        } else {
            return Flux.error(new UnauthorizedException("Access denied!"));
        }
    }

    @DeleteMapping("/{username}")
    public Mono<UserDto> deleteUserByUsername(Authentication authentication, @PathVariable String username) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()))) {
            return userService.getUserByUsername(username).
                    flatMap(userService::deleteUser).
                    map(userMapper::map);
        } else {
            return Mono.error(new UnauthorizedException("Access denied!"));
        }
    }

    @PatchMapping("/{username}")
    public Mono<UserDto> banUserByUsername(Authentication authentication, @PathVariable String username) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.MODERATOR.toString()))) {
            return userService.getUserByUsername(username).
                    flatMap(userService::banUser).
                    map(userMapper::map);
        } else {
            return Mono.error(new UnauthorizedException("Access denied!"));
        }
    }

    @PatchMapping("/unban/{username}")
    public Mono<UserDto> unbanUserByUsername(Authentication authentication, @PathVariable String username) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.MODERATOR.toString()))) {
            return userService.getUserByUsername(username).
                    flatMap(userService::unbanUser).
                    map(userMapper::map);
        } else {
            return Mono.error(new UnauthorizedException("Access denied!"));
        }
    }

    @PutMapping()
    public Mono<UserDto> updateUserByUsername(Authentication authentication, @RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.map(userDto);
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString()))) {
            return userService.updateUser(userEntity).map(userMapper::map);
        } else {
            return Mono.error(new UnauthorizedException("Access denied!"));
        }
    }
}
