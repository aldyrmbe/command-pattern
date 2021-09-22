package com.springwebflux.config;

import com.springwebflux.controller.UserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> appContext(){
        return RouterFunctions.route()
                .path("/app", builder -> builder
                        .GET("", serverRequest -> ServerResponse.ok().body(Mono.just("Hello World"), String.class))
                        .GET("/version", serverRequest -> ServerResponse.ok().body(Mono.just("0.0.1-1-SNAPSHOT"), String.class))
                        .build())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userControllerRouter(UserController userController){
        return RouterFunctions.route()
                .path("/users", builder -> builder
                        .GET("/{name}", userController::findByName)
                        .POST("", userController::save)
                        .PUT("", userController::update)
                        .DELETE("/{name}", userController::deleteByName)
                        .build())
                .build();
    }
}
