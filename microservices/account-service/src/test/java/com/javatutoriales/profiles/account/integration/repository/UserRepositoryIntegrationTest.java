package com.javatutoriales.profiles.account.integration.repository;

import com.javatutoriales.profiles.account.persistence.UserRepository;
import com.javatutoriales.profiles.account.config.DataSourceConfig;
import com.javatutoriales.profiles.account.config.FlywayConfig;
import com.javatutoriales.profiles.account.persistence.UserEntity;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataR2dbcTest
@Import({FlywayConfig.class, DataSourceConfig.class})
class UserRepositoryIntegrationTest {

    @Container
    private static MySQLContainer database = new MySQLContainer("mysql:8.0.28");

    @Autowired
    UserRepository repository;

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", UserRepositoryIntegrationTest::r2dbcUrl);
        registry.add("spring.r2dbc.username", database::getUsername);
        registry.add("spring.r2dbc.password", database::getPassword);

        registry.add("spring.flyway.url", database::getJdbcUrl);
        registry.add("spring.flyway.user", database::getUsername);
        registry.add("spring.flyway.password", database::getPassword);
    }

    private static String r2dbcUrl() {
        return String.format("r2dbc:mysql://%s:%s/%s",
                database.getContainerIpAddress(),
                database.getMappedPort(MySQLContainer.MYSQL_PORT),
                database.getDatabaseName());
    }

    @Test
    @Sql(scripts = "classpath:db/test/userRepository/populate.sql")
    void testRepository() {
        UserEntity user = UserEntity.builder()
                .firstName("test")
                .lastName("test")
                .username("user")
                .password("pass")
                .build();

        Publisher<UserEntity> setup = repository.deleteAll().then(repository.save(user));

        StepVerifier.create(setup)
                .consumeNextWith(userEntity -> assertThat(user.getId()).isPositive().isEqualTo(3))
                .verifyComplete();
    }
}