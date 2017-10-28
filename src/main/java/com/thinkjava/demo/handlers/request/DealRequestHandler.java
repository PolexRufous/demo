package com.thinkjava.demo.handlers.request;

import com.github.javafaker.Code;
import com.github.javafaker.Faker;
import com.github.javafaker.Superhero;
import com.thinkjava.demo.entities.Deal;
import com.thinkjava.demo.entities.Hero;
import com.thinkjava.demo.entities.ProductType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Component
public class DealRequestHandler {

  private Superhero superhero = new Faker().superhero();
  private Code code = new Faker().code();
  private Random random = new Random();
  private static final int PRICE_TRESHOLD = 1000000;

  @Nonnull
  public Mono<ServerResponse> getCurrentDeals(ServerRequest request) {
    Stream<Deal> dealStream = Stream.generate(this::getRandomDeal);
    Flux<Deal> dealFlux = Flux.fromStream(dealStream).delayElements(Duration.ofMillis(200));

    return ServerResponse.ok()
        .header("Access-Control-Allow-Origin", "*")
        .contentType(TEXT_EVENT_STREAM)
        .body(BodyInserters.fromPublisher(dealFlux, Deal.class));
  }

  private Deal getRandomDeal() {
    return new Deal()
        .setBuyer(getRandomHero())
        .setSeller(getRandomHero())
        .setId(code.imei())
        .setPrice(Math.ceil(random.nextDouble() * PRICE_TRESHOLD))
        .setProductType(getRandomProductType())
        .setDescription("bla-bla-bla");
  }

  private Hero getRandomHero() {
    return new Hero()
        .setName(superhero.name())
        .setDescription(superhero.descriptor())
        .setPower(superhero.power())
        .setId(code.imei());
  }

  private ProductType getRandomProductType() {
    return ProductType.values()[random.nextInt(ProductType.values().length)];
  }
}
