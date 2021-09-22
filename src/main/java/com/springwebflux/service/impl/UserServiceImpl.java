package com.springwebflux.service.impl;

import com.springwebflux.model.entity.User;
import com.springwebflux.repository.UserRepository;
import com.springwebflux.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public Mono<User> findByName(String name) {
        return userRepository.findFirstByName(name);
    }

    @Override
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<User> update(String name, String job) {
        return findByName(name).map(o -> {
                    o.setJob(job);
                    return o;
                })
                .flatMap(this::save);
    }

    @Override
    public Mono<User> delete(String name) {
        return userRepository.deleteByName(name);
    }
}
