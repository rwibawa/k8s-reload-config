package com.avisow.spring5.config;

import com.avisow.spring5.component.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class MyRoutes {

    @Bean
    RouterFunction<ServerResponse> home() {
        return RouterFunctions.route(GET("/"), request -> ok().body(fromValue("Home page")));
    }

    @Bean
    public RouterFunction<ServerResponse> hello(GreetingHandler greetingHandler) {

        return RouterFunctions.route(
                GET("/hello")
                .and(
                    RequestPredicates.accept(MediaType.TEXT_PLAIN)
                ),
                greetingHandler::hello);
    }
}