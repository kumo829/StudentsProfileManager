package com.javatutoriales.profiles.account.services;

import com.javatutoriales.profiles.account.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> getUser(String username);

    Mono<User> getUser(Long id);

    Mono<User> saveUser(Mono<User> user);

    Flux<User> getAll();
}
