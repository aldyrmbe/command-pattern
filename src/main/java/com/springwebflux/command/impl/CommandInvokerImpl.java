package com.springwebflux.command.impl;

import com.springwebflux.command.Command;
import com.springwebflux.command.CommandInvoker;
import com.springwebflux.model.command.CommandRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class CommandInvokerImpl implements CommandInvoker, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // This nya penting
        this.applicationContext = applicationContext;
    }

    @Override
    public <REQUEST extends CommandRequest, RESPONSE> Mono<RESPONSE> invokeCommand(Class<? extends Command<REQUEST, RESPONSE>> commandClass, REQUEST request) {
        Command<REQUEST, RESPONSE> bean = applicationContext.getBean(commandClass);
        return bean.execute(request);
    }
}
