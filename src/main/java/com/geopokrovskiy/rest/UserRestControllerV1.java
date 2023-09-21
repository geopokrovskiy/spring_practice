package com.geopokrovskiy.rest;

import com.geopokrovskiy.dto.UserDto;
import com.geopokrovskiy.entity.UserEntity;
import com.geopokrovskiy.entity.UserRole;
import com.geopokrovskiy.exception.AuthException;
import com.geopokrovskiy.mapper.UserMapper;
import com.geopokrovskiy.security.CustomPrincipal;
import com.geopokrovskiy.security.JwtHandler;
import com.geopokrovskiy.security.SecurityService;
import com.geopokrovskiy.security.TokenDetails;
import com.geopokrovskiy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserRestControllerV1 {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;
    @GetMapping
    public Flux<UserDto> getAllUsers(Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        String username = principal.getUsername();
        UserEntity user = userService.getUserByUsername(username).block();
        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MODERATOR){
            return userService.getAllUsers().map(userMapper::map);
        } else {
            throw new AuthException("You must be a moderator or an admin!", "NO RIGHTS EXCEPTION");
        }
    }
}
