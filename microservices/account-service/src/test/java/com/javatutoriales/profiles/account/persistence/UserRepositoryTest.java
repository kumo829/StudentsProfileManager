package com.javatutoriales.profiles.account.persistence;

import com.javatutoriales.profiles.account.UserRepository;
import com.javatutoriales.profiles.account.config.DataSourceConfig;
import com.javatutoriales.profiles.account.config.FlywayConfig;
import com.javatutoriales.profiles.account.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Import({FlywayConfig.class, DataSourceConfig.class})
@Sql(scripts = "classpath:db/test/userRepository/populate.sql")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    void testUserIdNotFirstOne_whenOtherUsersInDatabase() {
        UserEntity user = UserEntity.builder()
                .firstName("test")
                .lastName("test")
                .username("user")
                .password("pass")
                .build();

        Publisher<UserEntity> setup = repository.deleteAll().then(repository.save(user));

        StepVerifier.create(setup)
                .consumeNextWith(userEntity -> assertThat(user.getId()).isPositive().isGreaterThan(1))
                .verifyComplete();
    }

}