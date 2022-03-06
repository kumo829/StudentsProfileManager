package com.javatutoriales.profiles.account.persistence;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {

    @Override
    @Query("SELECT * FROM accounts WHERE id = :id AND deleted = false")
    Mono<UserEntity> findById(Long id);

    Mono<UserEntity> findByUsername(String username);
}
