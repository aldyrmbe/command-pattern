package com.springwebflux.command.impl;

import com.springwebflux.command.FindByNameCommand;
import com.springwebflux.model.command.FindByNameCommandRequest;
import com.springwebflux.model.entity.User;
import com.springwebflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class FindByNameCommandImpl implements FindByNameCommand {

    private final UserRepository userRepository;

    @Override
    public Mono<User> execute(FindByNameCommandRequest findByNameCommandRequest) {
        return userRepository.findFirstByName(findByNameCommandRequest.getName());
    }
}
