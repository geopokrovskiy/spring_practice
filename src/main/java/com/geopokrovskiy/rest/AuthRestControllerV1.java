package com.geopokrovskiy.rest;

import com.geopokrovskiy.dto.AuthRequestDto;
import com.geopokrovskiy.dto.AuthResponseDto;
import com.geopokrovskiy.dto.UserDto;
import com.geopokrovskiy.entity.UserEntity;
import com.geopokrovskiy.mapper.UserMapper;
import com.geopokrovskiy.security.CustomPrincipal;
import com.geopokrovskiy.security.SecurityService;
import com.geopokrovskiy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {
    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/register_user")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.map(dto);
        return userService.registerUser(entity)
                .map(userMapper::map);
    }

    @PostMapping("/register_admin")
    public Mono<UserDto> registerAdmin(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.map(dto);
        return userService.registerAdmin(entity)
                .map(userMapper::map);
    }

    @PostMapping("/register_moderator")
    public Mono<UserDto> registerMOderator(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.map(dto);
        return userService.registerModerator(entity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

        return userService.getUserById(customPrincipal.getId())
                .map(userMapper::map);
    }

}
