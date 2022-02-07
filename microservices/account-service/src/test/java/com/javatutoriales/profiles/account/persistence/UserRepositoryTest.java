package com.javatutoriales.profiles.account.persistence;

import com.javatutoriales.profiles.account.config.DataSourceConfig;
import com.javatutoriales.profiles.account.config.FlywayConfig;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Import({FlywayConfig.class, DataSourceConfig.class})
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    @Sql(scripts = "classpath:db/test/userRepository/populate.sql")
    void testUserIdNotFirstOne_whenOtherUsersInDatabase() {
        UserEntity user = UserEntity.builder()
                .firstName("test")
                .lastName("test")
                .username("user")
                .password("pass")
                .build();

        Publisher<UserEntity> setup = repository.save(user);

        StepVerifier.create(setup)
                .consumeNextWith(userEntity -> assertThat(user.getId()).isPositive().isEqualTo(3))
                .verifyComplete();
    }


    @Test
    @Sql(scripts = "classpath:db/test/userRepository/populate.sql")
    void testException_whenUsernameAlreadyExists() {
        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Johnson")
                .username("john123@gmail.com")
                .password("asdf87df9adfhjasfa")
                .build();

        Publisher<UserEntity> setup = repository.save(user);

        StepVerifier.create(setup)
                .expectErrorMatches(throwable -> throwable instanceof DataIntegrityViolationException)
                .verify();
    }

    @Test
    void testSave50Users() {
        Publisher<UserEntity> setup = Flux.range(1, 50).map(index -> UserEntity.builder()
                        .firstName("Name " + index)
                        .lastName("LastName " + index)
                        .username("user" + index + "@gmail.com")
                        .password("asdf87df9adfhjasfa" + index)
                        .build())
                .flatMap(user -> repository.save(user)).log();

        StepVerifier.create(setup)
                //.consumeNextWith(userEntity -> assertThat(userEntity.getId()).isPositive())
                //.recordWith(ArrayList::new)
                .expectNextCount(50)
//                .consumeRecordedWith(userList -> {
//
//                })
                .verifyComplete();

    }
}