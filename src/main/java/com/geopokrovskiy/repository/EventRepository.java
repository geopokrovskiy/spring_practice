package com.geopokrovskiy.repository;

import com.geopokrovskiy.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EventRepository extends R2dbcRepository<EventEntity, Integer> {
    Flux<EventEntity> findAllByUserId(Integer userId);
}
