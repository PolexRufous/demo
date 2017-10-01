package com.thinkjava.demo.handlers.request;

import com.thinkjava.demo.entities.Person;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
public class PersonRequestHandler {

    @Nonnull
    public Mono<ServerResponse> getSimple(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromObject(new Person()
                        .setId("qwerty1")
                        .setMissCount(12L)));
    }

    @Nonnull
    public Mono<ServerResponse> getShortStream(ServerRequest serverRequest) {
        Stream<Person> personStream = Stream.generate(new Random()::nextInt)
                .limit(5)
                .map(String::valueOf)
                .map(this::getPersonById);
        return ServerResponse.ok()
                .header("Access-Control-Allow-Origin", "*")
                .contentType(TEXT_EVENT_STREAM)
                .body(fromPublisher(Flux.fromStream(personStream).delayElements(Duration.ofMillis(1000)), Person.class));
    }

    private Person getPersonById(String id) {
        Random random = new Random();
        return new Person()
                .setId(id)
                .setMissCount(Integer.valueOf(random.nextInt(100)).longValue());
    }

    private String getRandomGender() {
        return new Random().nextBoolean() ? "Male" : "Female";
    }
}
