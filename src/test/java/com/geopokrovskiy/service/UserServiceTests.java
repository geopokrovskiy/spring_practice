package com.geopokrovskiy.service;


import com.geopokrovskiy.entity.EventEntity;
import com.geopokrovskiy.entity.Status;
import com.geopokrovskiy.entity.UserEntity;
import com.geopokrovskiy.entity.UserRole;
import com.geopokrovskiy.repository.EventRepository;
import com.geopokrovskiy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class UserServiceTests {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final EventRepository eventRepository = Mockito.mock(EventRepository.class);
    @InjectMocks
    private UserService userService;
    private final LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 29, 14, 12);

    private UserEntity getUser() {
        UserEntity user1 = UserEntity.builder()
                .id(1)
                .firstName("fn1")
                .lastName("ln1")
                .username("un1")
                .enabled(true)
                .status(Status.ACTIVE)
                .createdAt(localDateTime)
                .role(UserRole.USER)
                .password("pwd1")
                .build();
        return user1;
    }

    private Flux<UserEntity> getUsers() {
        UserEntity user1 = UserEntity.builder()
                .id(1)
                .firstName("fn1")
                .lastName("ln1")
                .username("un1")
                .enabled(true)
                .status(Status.ACTIVE)
                .createdAt(localDateTime)
                .role(UserRole.USER)
                .password("pwd1")
                .build();
        UserEntity user2 = UserEntity.builder()
                .id(2)
                .firstName("fn2")
                .lastName("ln2")
                .username("un2")
                .enabled(true)
                .status(Status.ACTIVE)
                .createdAt(localDateTime)
                .role(UserRole.USER)
                .password("pwd2")
                .build();
        UserEntity user3 = UserEntity.builder()
                .id(3)
                .firstName("fn3")
                .lastName("ln3")
                .username("un3")
                .enabled(true)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .role(UserRole.USER)
                .password("pwd3")
                .build();

        Flux<UserEntity> users = Flux.just(user1, user2, user3);
        return users;
    }

    private Flux<EventEntity> getEvents(){
        EventEntity event1 = EventEntity.builder()
                .id(1)
                .userId(1)
                .fileId(1)
                .build();

        EventEntity event2 = EventEntity.builder()
                .id(2)
                .userId(2)
                .fileId(2)
                .build();

        return Flux.just(event1, event2);
    }

    @Test
    public void testGetAll() {
        Flux<UserEntity> users = this.getUsers();

        Mockito.when(this.userRepository.findAll()).thenReturn(users);
        Mockito.when(this.eventRepository.findAllByUserId(1)).thenReturn(getEvents());
        Flux<UserEntity> result = userService.getAllUsers();
        StepVerifier.create(result).expectNextCount(3);
    }

    @Test
    public void testGetUserById() {
        Mockito.when(this.userRepository.findById(1)).thenReturn(Mono.just(this.getUser()));
        Mockito.when(this.eventRepository.findAllByUserId(1)).thenReturn(getEvents());
        Mono<UserEntity> actualResult = userService.getUserById(1);
        UserEntity expectedResult = this.getUser();

        StepVerifier.create(actualResult)
                .expectNext(expectedResult)
                .expectComplete();
    }

    @Test
    public void testGetUserByUserName() {
        Mockito.when(this.userRepository.findByUsername("un1")).thenReturn(Mono.just(this.getUser()));
        Mockito.when(this.eventRepository.findAllByUserId(1)).thenReturn(getEvents());
        Mono<UserEntity> actualResult = userService.getUserByUsername("un1");
        UserEntity expectedResult = this.getUser();

        StepVerifier.create(actualResult)
                .expectNext(expectedResult)
                .expectComplete();
    }

    @Test
    public void testRegisterUser() {
        UserEntity newUser = UserEntity.builder()
                .firstName("fn")
                .lastName("ln")
                .username("un")
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .password("pwd")
                .enabled(false).build();

        UserEntity expectedUser = newUser.toBuilder()
                .password("encodedPassword")
                .role(UserRole.USER)
                .enabled(true)
                .status(Status.ACTIVE)
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .build();

        Mockito.when(this.userRepository.save(Mockito.any(UserEntity.class))).thenReturn(Mono.just(expectedUser));
        Mockito.when(passwordEncoder.encode("pwd")).thenReturn("encodedPwd");

        Mono<UserEntity> result = userService.registerUser(newUser);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getPassword().equals("encodedPassword"))
                .expectNextMatches(user -> user.getRole().equals(UserRole.USER))
                .expectNextMatches(user -> user.isEnabled())
                .expectNextMatches(user -> user.getStatus().equals(Status.ACTIVE))
                .expectNextMatches(user -> user.getCreatedAt() != null)
                .expectNextMatches(user -> user.getUpdatedAt() != null)
                .expectComplete();
    }
}
