package com.william.devx.webflux.reactive.controller;

import com.william.devx.webflux.reactive.domain.Restaurant;
import com.william.devx.webflux.reactive.repositories.RestaurantRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Created by sungang on 2018/4/4.
 */
@RestController
public class RestaurantController {


    /**
     * 扩展ReactiveCrudRepository接口，提供基本的CRUD操作
     */
    private final RestaurantRepository restaurantRepository;

    /**
     * spring-boot-starter-data-mongodb-reactive提供的通用模板
     */
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public RestaurantController(RestaurantRepository restaurantRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.restaurantRepository = restaurantRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @GetMapping("/reactive/restaurants")
    public Flux<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/reactive/delay/restaurants")
    public Flux<Restaurant> findAllDelay() {
        return restaurantRepository.findAll().delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("/reactive/restaurants/{id}")
    public Mono<Restaurant> get(@PathVariable String id) {
        return restaurantRepository.findById(id);
    }

    @PostMapping("/reactive/restaurants")
    public Flux<Restaurant> create(@RequestBody Flux<Restaurant> restaurants) {
        return restaurants
                .buffer(10000)
                .flatMap(rs -> reactiveMongoTemplate.insert(rs, Restaurant.class));
    }

    @DeleteMapping("/reactive/restaurants/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return restaurantRepository.deleteById(id);
    }
}
