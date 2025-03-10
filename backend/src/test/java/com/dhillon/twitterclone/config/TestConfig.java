package com.dhillon.twitterclone.config;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test configuration for WebMvcTest to properly handle JPA entity dependencies.
 * This configuration mocks the JPA infrastructure to avoid "JPA metamodel must not be empty" errors.
 */
@TestConfiguration
@EntityScan(basePackages = "com.dhillon.twitterclone.entity")
@EnableJpaRepositories(basePackages = "com.dhillon.twitterclone.repository")
public class TestConfig {
    
    /**
     * Mock the JPA metamodel context to avoid "JPA metamodel must not be empty" errors
     * when running WebMvcTest slice tests.
     */
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    
    /**
     * Create PostgreSQL container for testing.
     * The container will be started when the bean is initialized.
     *
     * @return the PostgreSQL container
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
                .withDatabaseName("twitter_clone_test")
                .withUsername("testuser")
                .withPassword("testpass");
    }
    
    /**
     * Provides a password encoder instance for tests.
     * 
     * @return A BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 