package com.springwebflux.command.impl;

import com.springwebflux.command.DeleteCommand;
import com.springwebflux.model.command.DeleteRequest;
import com.springwebflux.model.entity.User;
import com.springwebflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class DeleteCommandImpl implements DeleteCommand {
    private final UserRepository userRepository;

    @Override
    public Mono<User> execute(DeleteRequest request) {
        return userRepository.deleteByName(request.getName());
    }
}
