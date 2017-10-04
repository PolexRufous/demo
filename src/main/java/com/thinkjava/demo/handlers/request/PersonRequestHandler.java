package com.thinkjava.demo.handlers.request;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.github.javafaker.Faker;
import com.thinkjava.demo.entities.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PersonRequestHandler {

    @Nonnull
    public Mono<ServerResponse> getDetailed(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        String name = new Faker().name().fullName();
        String gender = getRandomGender();
        Mono<Person> person = Mono.just(new Person().setId(id).setGender(gender).setName(name));
        return ServerResponse.ok()
            .header("Access-Control-Allow-Origin", "*")
            .contentType(APPLICATION_JSON)
            .body(person.delayElement(Duration.ofSeconds(3)), Person.class);
    }

    @Nonnull
    public Mono<ServerResponse> getShortStream(ServerRequest serverRequest) {
        Stream<Person> personStream = Stream.generate(new Random()::nextInt)
                .limit(15)
                .map(String::valueOf)
                .map(this::getPersonById);
        Flux<Person> flux = Flux.fromStream(personStream).delayElements(Duration.ofSeconds(2));
        return ServerResponse.ok()
                .header("Access-Control-Allow-Origin", "*")
                .contentType(TEXT_EVENT_STREAM)
                .body(fromPublisher(flux, Person.class));
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
