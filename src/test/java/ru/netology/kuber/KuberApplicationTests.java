package ru.netology.kuber;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KuberApplicationTests {

    private static final GenericContainer<?> devapp = new GenericContainer<>("devapp")
            .withExposedPorts(8080);

    private static final GenericContainer<?> prodapp = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @AfterAll
    public static void tearDown() {
        devapp.stop();
        prodapp.stop();
    }

    @Test
    void devProfileReturnsDev() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                "http://localhost:" + devapp.getMappedPort(8080) + "/profile", String.class);
        System.out.println(forEntity.getBody());

        assertEquals(HttpStatus.OK, forEntity.getStatusCode());
        assertEquals("Current profile is dev", forEntity.getBody());
    }

    @Test
    void prodProfileReturnsProduction() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                "http://localhost:" + prodapp.getMappedPort(8081) + "/profile", String.class);
        System.out.println(forEntity.getBody());

        assertEquals(HttpStatus.OK, forEntity.getStatusCode());
        assertEquals("Current profile is production", forEntity.getBody());
    }
}
