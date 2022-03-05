package com.javatutoriales.profiles.account.services;

import com.javatutoriales.profiles.account.model.User;
import com.javatutoriales.profiles.account.model.mappers.UserEntityMapper;
import com.javatutoriales.profiles.account.persistence.UserRepository;
import com.javatutoriales.profiles.commons.errors.http.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserEntityMapper userMapper;

    @Override
    public Mono<User> getUser(String username) {
        return null;
    }

    @Override
    public Mono<User> getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::entityToModel)
                .onErrorMap(DataIntegrityViolationException.class, throwable -> new NotFoundException("User not found: " + id, throwable));
    }

    @Override
    public Mono<User> saveUser(Mono<User> user) {
        return user.map(userMapper::modelToEntity)
                .flatMap(userRepository::save)
                .map(userMapper::entityToModel);

    }

    @Override
    public Flux<User> getAll() {
        return userRepository.findAll()
                .map(userMapper::entityToModel);
    }
}
