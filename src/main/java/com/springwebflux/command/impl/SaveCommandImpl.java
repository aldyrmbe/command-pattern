package com.springwebflux.command.impl;

import com.springwebflux.command.SaveCommand;
import com.springwebflux.model.command.SaveCommandRequest;
import com.springwebflux.model.entity.User;
import com.springwebflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class SaveCommandImpl implements SaveCommand {

    private final UserRepository userRepository;

    @Override
    public Mono<User> execute(SaveCommandRequest saveCommandRequest) {
        User user = User.builder()
                .name(saveCommandRequest.getName())
                .job(saveCommandRequest.getJob())
                .build();
        return userRepository.save(user);
    }
}
