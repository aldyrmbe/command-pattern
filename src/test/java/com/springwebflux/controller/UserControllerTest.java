package com.springwebflux.controller;

import com.springwebflux.model.entity.User;
import com.springwebflux.model.request.UserRequest;
import com.springwebflux.model.response.UserResponse;
import com.springwebflux.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll().subscribe();
    }

    @Test
    public void findByName_Test(){
        User user = User.builder()
                .name("Rambe")
                .job("Employee")
                .build();

        // JANGAN LUPA DI SUBSCRIBE
        userRepository.save(user).subscribe();

        UserResponse response = webTestClient
                .get().uri("/users/Rambe")
                .exchange()
                .expectBody(UserResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Rambe", response.getName());
        assertEquals("Employee", response.getJob());
    }

    @Test
    public void save_Test(){
        UserRequest request = UserRequest.builder()
                .name("Rambe")
                .job("Employee")
                .build();

        UserResponse response = webTestClient
                .post().uri("/users")
                .body(Mono.just(request), UserRequest.class)
                .exchange()
                .expectBody(UserResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Rambe", response.getName());
        assertEquals("Employee", response.getJob());

        User user = userRepository.findFirstByName("Rambe").block();
        assertEquals("Rambe", user.getName());
        assertEquals("Employee", user.getJob());
    }

    @Test
    public void update_Test(){
        User user = User.builder()
                .name("Rambe")
                .job("Employee")
                .build();

        // JANGAN LUPA DI SUBSCRIBE
        userRepository.save(user).subscribe();

        UserResponse response = webTestClient
                .put()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/users")
                                .queryParam("name", "Rambe")
                                .queryParam("job", "SDE")
                                .build()
                )
                .exchange()
                .expectBody(UserResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Rambe", response.getName());
        assertEquals("SDE", response.getJob());

        User savedUser = userRepository.findFirstByName("Rambe").block();
        assertEquals("Rambe", savedUser.getName());
        assertEquals("SDE", savedUser.getJob());
    }

    @Test
    public void delete_Test(){
        User user = User.builder()
                .name("Rambe")
                .job("Employee")
                .build();

        // JANGAN LUPA DI SUBSCRIBE
        userRepository.save(user).subscribe();

        Boolean response = webTestClient
                .delete()
                .uri("/users/Rambe")
                .exchange()
                .expectBody(Boolean.class)
                .returnResult()
                .getResponseBody();

        assertTrue(response);

        User deletedUser = userRepository.findFirstByName("Rambe").block();
        assertNull(deletedUser);
    }
}
