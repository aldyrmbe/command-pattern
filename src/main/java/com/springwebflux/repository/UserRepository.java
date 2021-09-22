package com.springwebflux.repository;

import com.springwebflux.model.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findFirstByName(String name);
    Mono<User> deleteByName(String name);
}
