package com.javatutoriales.profiles.account.controllers;

import com.javatutoriales.profiles.account.controllers.vos.UserRequest;
import com.javatutoriales.profiles.account.controllers.vos.UserResponse;
import com.javatutoriales.profiles.account.controllers.vos.mappers.UserVOMapper;
import com.javatutoriales.profiles.account.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private static final String API_URL = "/api/v1/accounts/";

    private final UserVOMapper userMapper;
    private final UserService userService;

    @GetMapping("/{id}")
    Mono<ResponseEntity<UserResponse>> getUser(@Positive @PathVariable Long id) {
        return userService.getUser(id)
                .map(userMapper::modelToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    Mono<ResponseEntity<UserResponse>> registerAccount(@RequestBody @Valid UserRequest user) {
        return userService
                .saveUser(userMapper.requestToModel(user))
                .map(userMapper::modelToResponse)
                .map(userResponse -> ResponseEntity
                        .created(URI.create(API_URL + userResponse.id()))
                        .body(userResponse));
    }

}
