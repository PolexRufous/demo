package com.thinkjava.demo.configuration;

import com.thinkjava.demo.handlers.request.DealRequestHandler;
import com.thinkjava.demo.handlers.request.PersonRequestHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ServerConfiguration {

  @NonNull
  private final PersonRequestHandler personRequestHandler;
  @NonNull
  private final DealRequestHandler dealRequestHandler;

  @Bean
  public HttpServer server(RouterFunction<?> router) {
    HttpServer server = HttpServer.create(8091);
    HttpHandler httpHandler = RouterFunctions.toHttpHandler(router);
    server.start(new ReactorHttpHandlerAdapter(httpHandler));
    return server;
  }

  @Bean
  public RouterFunction<ServerResponse> router() {
    return route(GET("/persons"), personRequestHandler::getShortStream)
        .andRoute(GET("/persons/{id}"), personRequestHandler::getDetailed)
        .andRoute(GET("/deals"), dealRequestHandler::getCurrentDeals);
  }
}
