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

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper userMapper;


    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .doOnNext(userEntity -> {
                    userEntity.setDeleted(true);
                    userEntity.setDeletedOn(ZonedDateTime.now());
                })
                .flatMap(userRepository::save).then();
    }

    @Override
    public Mono<Void> disableUser(Long id, boolean disabled) {
        return userRepository.findById(id)
                .doOnNext(userEntity -> {
                    userEntity.setDisabled(disabled);
                    userEntity.setDisabledOn(disabled ? ZonedDateTime.now() : null);
                })
                .flatMap(userRepository::save)
                .then();
    }

    @Override
    public Flux<User> getAll() {
        return userRepository.findAll()
                .map(userMapper::entityToModel);
    }

    @Override
    public Mono<User> getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::entityToModel)
                .onErrorMap(DataIntegrityViolationException.class, throwable -> new NotFoundException("User not found: " + id, throwable));
    }

    @Override
    public Mono<User> getUser(String username) {
        return userRepository
                .findByUsername(username)
                .map(userMapper::entityToModel);
    }

    @Override
    public Mono<User> saveUser(Mono<User> user) {
        return user.map(userMapper::modelToEntity)
                .flatMap(userRepository::save)
                .map(userMapper::entityToModel);
    }

    @Override
    public Mono<User> updateUser(Long id, Mono<User> user) {
        return userRepository.findById(id)
                .flatMap(u -> user.map(userMapper::modelToEntity))
                .doOnNext(entityUser -> entityUser.setId(id))
                .flatMap(userRepository::save)
                .map(userMapper::entityToModel);
    }
}
