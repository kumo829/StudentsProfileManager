package com.javatutoriales.profiles.account.services;

import com.javatutoriales.profiles.account.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Void> deleteUser(Long id);

    Mono<Void> disableUser(Long id, boolean disabled);

    Flux<User> getAll();

    Mono<User> getUser(Long id);

    Mono<User> getUser(String username);

    Mono<User> saveUser(Mono<User> user);

    Mono<User> updateUser(Long id, Mono<User> user);
}
