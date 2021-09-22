package com.springwebflux.service;

import com.springwebflux.model.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> findByName(String name);
    Mono<User> save(User user);
    Mono<User> update(String name, String job);
    Mono<User> delete(String name);
}
