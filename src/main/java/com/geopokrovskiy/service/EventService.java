package com.geopokrovskiy.service;

import com.geopokrovskiy.entity.EventEntity;
import com.geopokrovskiy.entity.EventString;
import com.geopokrovskiy.entity.FileEntity;
import com.geopokrovskiy.entity.UserEntity;
import com.geopokrovskiy.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    public Mono<EventEntity> addEvent(FileEntity fileEntity, UserEntity userEntity, EventString eventString){
        EventEntity eventEntity = new EventEntity()
                .toBuilder()
                .fileId(fileEntity.getId())
                .userId(userEntity.getId())
                .eventString(eventString)
                .build();
        return eventRepository.save(eventEntity);
    }
}
