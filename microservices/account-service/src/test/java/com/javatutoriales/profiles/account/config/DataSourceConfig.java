package com.javatutoriales.profiles.account.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@TestConfiguration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final Environment env;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(env.getRequiredProperty("spring.flyway.url"));
        dataSourceBuilder.username(env.getRequiredProperty("spring.flyway.user"));
        dataSourceBuilder.password(env.getRequiredProperty("spring.flyway.password"));
        return dataSourceBuilder.build();
    }
}
