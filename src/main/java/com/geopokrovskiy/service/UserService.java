package com.geopokrovskiy.service;

import com.geopokrovskiy.entity.EventEntity;
import com.geopokrovskiy.entity.Status;
import com.geopokrovskiy.entity.UserEntity;
import com.geopokrovskiy.entity.UserRole;
import com.geopokrovskiy.repository.EventRepository;
import com.geopokrovskiy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> {
            log.info("IN registerUser - user: {} created", u);
        });
    }

    public Mono<UserEntity> registerAdmin(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.ADMIN)
                        .enabled(true)
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> {
            log.info("IN registerAdmin - user : {} created", u);
        });
    }

    public Mono<UserEntity> registerModerator(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.MODERATOR)
                        .enabled(true)
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> {
            log.info("IN registerModerator - user : {} created", u);
        });
    }

    public Mono<UserEntity> getUserById(Integer id) {
        return Mono.zip(userRepository.findById(id), eventRepository.findAllByUserId(id).collectList())
                .map(tuples -> {
                    UserEntity result = tuples.getT1();
                    List<EventEntity> events = tuples.getT2();
                    result.setEvents(events);
                    return result;
                });
    }

    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username).flatMap(user -> Mono.zip(
                Mono.just(user),
                eventRepository.findAllByUserId(user.getId()).collectList())
                .map(tuples -> {
                    UserEntity result = tuples.getT1();
                    List<EventEntity> events = tuples.getT2();
                    result.setEvents(events);
                    return result;
                }));
    }

    public Flux<UserEntity> getAllUsers() {
        return userRepository.findAll()
                .flatMap(user -> Mono.zip(Mono.just(user), eventRepository.findAllByUserId(user.getId()).collectList()))
                .map(tuples -> {
                    UserEntity result = tuples.getT1();
                    List<EventEntity> events = tuples.getT2();
                    result.setEvents(events);
                    log.info("All users has been received by an admin!");
                    return result;
                });
    }

    public Mono<UserEntity> deleteUser(UserEntity user) {
        return userRepository.save(
                        user.toBuilder()
                                .status(Status.DELETED)
                                .enabled(false)
                                .updatedAt(LocalDateTime.now())
                                .build())
                .doOnSuccess(u -> {
                    log.info("User : {} has been deleted", u);
                });
    }

    public Mono<UserEntity> banUser(UserEntity user) {
        return userRepository.save(
                        user.toBuilder()
                                .enabled(false)
                                .updatedAt(LocalDateTime.now())
                                .build())
                .doOnSuccess(u -> {
                    log.info("User : {} has been banned!", u);
                });
    }

    public Mono<UserEntity> unbanUser(UserEntity user) {
        return userRepository.save(
                        user.toBuilder()
                                .enabled(true)
                                .updatedAt(LocalDateTime.now())
                                .build())
                .doOnSuccess(u -> {
                    log.info("User : {} has been unbanned!", u);
                });
    }

    public Mono<UserEntity> updateUser(UserEntity user) {
        return userRepository.findById(user.getId())
                .flatMap(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    existingUser.setRole(user.getRole());
                    existingUser.setEnabled(true);
                    existingUser.setStatus(Status.ACTIVE);
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(existingUser);
                })
                .doOnSuccess(updatedUser -> {
                    log.info("User: {} has been updated!", updatedUser);
                });
    }
}
