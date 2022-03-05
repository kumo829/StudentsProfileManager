package com.javatutoriales.profiles.account.controllers;

import com.javatutoriales.profiles.account.controllers.vos.UserRequest;
import com.javatutoriales.profiles.account.controllers.vos.UserResponse;
import com.javatutoriales.profiles.account.controllers.vos.mappers.UserVOMapper;
import com.javatutoriales.profiles.account.model.User;
import com.javatutoriales.profiles.account.persistence.UserRepository;
import com.javatutoriales.profiles.account.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private UserVOMapper userVOMapper;

    @MockBean
    private UserRepository userRepository;

    @Test
    void whenANewUserRequestIsReceived_thenResponseStatusIsCreated() {

        UserRequest userRequest = new UserRequest("First", "Last", "username", "pass");
        Mono<UserRequest> userRequestMono = Mono.just(userRequest);

        User user = User.builder().firstName("First").lastName("Last").username("username").password("pass").build();
        Mono<User> userMono = Mono.just(user);

        UserResponse userResponse = new UserResponse(1, "username");

        given(userVOMapper.requestToModel(userRequest)).willReturn(user);
        given(userService.saveUser(any(Mono.class))).willReturn(userMono);
        given(userVOMapper.modelToResponse(user)).willReturn(userResponse);

        webTestClient
                .post()
                .uri("/api/v1/accounts")
                .body(Mono.just(userRequestMono), UserRequest.class)
                .exchange()
                .expectStatus().isCreated();
    }
}