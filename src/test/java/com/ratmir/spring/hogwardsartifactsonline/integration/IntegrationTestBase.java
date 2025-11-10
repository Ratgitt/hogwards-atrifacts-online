package com.ratmir.spring.hogwardsartifactsonline.integration;

import com.ratmir.spring.hogwardsartifactsonline.integration.annotation.IT;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@IT
@Sql({
    "classpath:db/spring-course-sql/clean.sql",
    "classpath:db/spring-course-sql/data.sql"
})
//@Testcontainers
public abstract class IntegrationTestBase {

//    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");
//                .withInitScript("db/init-liquibase-schema.sql");

    @BeforeAll
    static void runContainer() {
        postgres.withInitScript("db/init-liquibase-schema.sql");
        postgres.start();
    }

//    @AfterAll
//    static void stopContainer() {
//        postgres.stop();
//    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
