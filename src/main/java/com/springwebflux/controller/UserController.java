package com.springwebflux.controller;

import com.springwebflux.command.*;
import com.springwebflux.model.command.DeleteRequest;
import com.springwebflux.model.command.FindByNameCommandRequest;
import com.springwebflux.model.command.SaveCommandRequest;
import com.springwebflux.model.command.UpdateCommandRequest;
import com.springwebflux.model.entity.User;
import com.springwebflux.model.request.UserRequest;
import com.springwebflux.model.response.UserResponse;
import com.springwebflux.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CommandInvoker commandInvoker;

    public Mono<ServerResponse> findByName(ServerRequest serverRequest){
        String name = serverRequest.pathVariable("name");
        FindByNameCommandRequest request = FindByNameCommandRequest.builder().name(name).build();
        Mono<User> user = commandInvoker.invokeCommand(FindByNameCommand.class, request);

        return ServerResponse.ok().body(user.map(this::toUserResponse), UserResponse.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<UserRequest> request = serverRequest.bodyToMono(UserRequest.class);
        // Awalnya Mono<Mono<User>> jadi Mono<User> karena pakai flatMap
        Mono<User> user = request.map(this::toUser).flatMap(req -> commandInvoker.invokeCommand(SaveCommand.class, req));
        return ServerResponse.ok().body(user.map(this::toUserResponse), UserResponse.class);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest){
        String name = serverRequest.queryParam("name").get();
        String job = serverRequest.queryParam("job").get();

        UpdateCommandRequest request = UpdateCommandRequest.builder()
                .name(name)
                .job(job)
                .build();

        Mono<User> user = commandInvoker.invokeCommand(UpdateCommand.class, request);

        return ServerResponse.ok()
                .body(user.map(this::toUserResponse), UserResponse.class);
    }

    public Mono<ServerResponse> deleteByName(ServerRequest serverRequest){
        String name = serverRequest.pathVariable("name");
        DeleteRequest request = DeleteRequest.builder().name(name).build();
        Mono<User> user = commandInvoker.invokeCommand(DeleteCommand.class, request);
        return ServerResponse.ok()
                .body(user.map(o -> true), Boolean.class);
    }

    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .name(user.getName())
                .job(user.getJob())
                .build();
    }

    private SaveCommandRequest toUser(UserRequest userRequest){
        return SaveCommandRequest.builder()
                .name(userRequest.getName())
                .job(userRequest.getJob())
                .build();
    }
}
