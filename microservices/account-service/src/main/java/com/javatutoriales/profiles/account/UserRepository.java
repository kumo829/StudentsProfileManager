package com.javatutoriales.profiles.account;

import com.javatutoriales.profiles.account.model.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
