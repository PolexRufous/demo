package com.thinkjava.demo.handlers.request;

import com.github.javafaker.Faker;
import com.github.javafaker.Superhero;
import com.thinkjava.demo.entities.Hero;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class HeroRequestHandler {

  private Superhero superhero = new Faker().superhero();

  @Nonnull
  public Mono<ServerResponse> findHero(ServerRequest serverRequest)
  {
    String id = serverRequest.pathVariable("id");
    Mono<Hero> heroMono = getHero(id);
    return ServerResponse.ok()
        .header("Access-Control-Allow-Origin", "*")
        .contentType(APPLICATION_JSON)
        .body(heroMono.delayElement(Duration.ofSeconds(3)), Hero.class);
  }

  private Mono<Hero> getHero(String id) {
    Hero hero = new Hero()
        .setId(id)
        .setName(superhero.name())
        .setPower(superhero.power())
        .setDescriptor(superhero.descriptor());
    return Mono.just(hero);
  }

}
