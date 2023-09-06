package com.geopokrovskiy.repository;

import com.geopokrovskiy.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, Integer> {
    Mono<UserEntity> findByUsername(String username);

}
