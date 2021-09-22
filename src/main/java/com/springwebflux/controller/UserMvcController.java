package com.springwebflux.controller;

import com.springwebflux.model.entity.User;
import com.springwebflux.model.response.UserResponse;
import com.springwebflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserMvcController {

    private final UserRepository userRepository;

    @GetMapping(path = "{name}")
    public Mono<UserResponse> findByName(@PathVariable("name") String name){
        return userRepository.findFirstByName(name).map(this::toUserResponse);
    }

    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .name(user.getName())
                .job(user.getJob())
                .build();
    }
}
