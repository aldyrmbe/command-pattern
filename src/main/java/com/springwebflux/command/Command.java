package com.springwebflux.command;

import com.springwebflux.model.command.CommandRequest;
import reactor.core.publisher.Mono;

public interface Command<REQUEST extends CommandRequest, RESPONSE> {
    Mono<RESPONSE> execute(REQUEST request);
}
