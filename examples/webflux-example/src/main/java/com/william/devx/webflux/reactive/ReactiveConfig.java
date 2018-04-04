package com.william.devx.webflux.reactive;

import com.william.devx.webflux.reactive.domain.Restaurant;
import com.william.devx.webflux.reactive.repositories.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

/**
 * Created by sungang on 2018/4/4.
 */
@Configuration
@EnableWebFlux
@EnableReactiveMongoRepositories
public class ReactiveConfig implements WebFluxConfigurer {

    @Bean
    public CommandLineRunner initData(RestaurantRepository restaurantRepository) {
        return args -> {
            restaurantRepository.deleteAll().block();
            Restaurant[] restaurants = IntStream.range(0, 3)
                    .mapToObj(String::valueOf)
                    .map(s -> new Restaurant(s, s, s))
                    .toArray(Restaurant[]::new);
            restaurantRepository.saveAll(Flux.just(restaurants)).subscribe();
        };
    }
}
