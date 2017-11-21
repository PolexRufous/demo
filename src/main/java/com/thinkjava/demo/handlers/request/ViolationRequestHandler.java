package com.thinkjava.demo.handlers.request;

import com.github.javafaker.Faker;
import com.thinkjava.demo.entities.*;
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
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
public class ViolationRequestHandler {

    private Faker faker = new Faker();

    @Nonnull
    public Mono<ServerResponse> getViolations(ServerRequest serverRequest) {
        Stream<Violation> violationStream = Stream.generate(new Random()::nextInt)
                .map(this::getViolation);
        Flux<Violation> flux = Flux.fromStream(violationStream).delayElements(Duration.ofSeconds(2));
        return ServerResponse.ok()
                .header("Access-Control-Allow-Origin", "*")
                .contentType(TEXT_EVENT_STREAM)
                .body(fromPublisher(flux, Violation.class));
    }

    private Violation getViolation(int random) {
        return new Violation()
                .setAddress(faker.address().secondaryAddress())
                .setReason(getRandomViolationReason(random))
                .setCarNumber(getRandomNumber())
                .setRegion(getRandomRegion(random));
    }

    private String getRandomViolationReason(int random) {
        return ViolationReason.values()[Math.abs(random) % ViolationReason.values().length].getName();
    }

    private String getRandomNumber() {
        return faker.code().ean8();
    }

    private String getRandomRegion(int random) {
        return ViolationRegion.values()[Math.abs(random) % ViolationRegion.values().length].getName();
    }

    @Nonnull
    public Mono<ServerResponse> findCar(ServerRequest serverRequest)
    {
        String number = serverRequest.pathVariable("number");
        Mono<Car> carMono = getCar(number);
        return ServerResponse.ok()
                .header("Access-Control-Allow-Origin", "*")
                .contentType(APPLICATION_JSON)
                .body(carMono.delayElement(Duration.ofSeconds(3)), Car.class);
    }

    private Mono<Car> getCar(String number) {
        Car car = new Car()
                .setNumber(number)
                .setOwnerFirstName(faker.name().firstName())
                .setOwnerLastName(faker.name().lastName())
                .setRegistrationAddress(faker.address().fullAddress());
        return Mono.just(car);
    }
}
