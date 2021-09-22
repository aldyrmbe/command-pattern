package com.springwebflux.command;

import com.springwebflux.model.command.CommandRequest;
import reactor.core.publisher.Mono;

public interface CommandInvoker {
    <REQUEST extends CommandRequest, RESPONSE> Mono<RESPONSE> invokeCommand(Class<? extends Command<REQUEST, RESPONSE>> commandClass, REQUEST request);
}
