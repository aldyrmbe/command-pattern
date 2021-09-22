package com.springwebflux.command.impl;

import com.springwebflux.command.UpdateCommand;
import com.springwebflux.model.command.UpdateCommandRequest;
import com.springwebflux.model.entity.User;
import com.springwebflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UpdateCommandImpl implements UpdateCommand {

    private final UserRepository userRepository;

    @Override
    public Mono<User> execute(UpdateCommandRequest request) {
        return userRepository.findFirstByName(request.getName())
                .map(o -> {
                    o.setJob(request.getJob());
                    return o;
                })
                .flatMap(userRepository::save);
    }
}
