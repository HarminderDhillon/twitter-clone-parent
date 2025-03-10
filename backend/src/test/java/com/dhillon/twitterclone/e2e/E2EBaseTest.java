package com.dhillon.twitterclone.e2e;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base class for all E2E tests.
 * Sets up Spring Boot with a random port and configures REST Assured
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class E2EBaseTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        // Set the basePath to match the context-path in application.yml
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL));
    }

    protected RequestSpecification givenJson() {
        return RestAssured.given()
                .contentType(ContentType.JSON);
    }
} 