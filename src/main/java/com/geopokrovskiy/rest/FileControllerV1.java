package com.geopokrovskiy.rest;

import com.geopokrovskiy.dto.EventDto;
import com.geopokrovskiy.dto.FileDto;
import com.geopokrovskiy.entity.FileEntity;
import com.geopokrovskiy.entity.UserEntity;
import com.geopokrovskiy.mapper.EventMapper;
import com.geopokrovskiy.mapper.FileMapper;
import com.geopokrovskiy.mapper.UserMapper;
import com.geopokrovskiy.security.CustomPrincipal;
import com.geopokrovskiy.security.SecurityService;
import com.geopokrovskiy.service.EventService;
import com.geopokrovskiy.service.FileService;
import com.geopokrovskiy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileControllerV1 {
    private final UserService userService;
    private final FileService fileService;
    private final FileMapper fileMapper;
    private final EventMapper eventMapper;

    @PostMapping("/upload")
    public Mono<EventDto> uploadFile1(Authentication authentication, @RequestBody FileDto fileDto){
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        String username = customPrincipal.getUsername();

        FileEntity fileEntity = fileMapper.map(fileDto);
        return userService.getUserByUsername(username)
                .flatMap(userEntity -> fileService.uploadFile(fileEntity, userEntity))
                .map(eventMapper::map);
    }
}
