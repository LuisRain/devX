package com.william.devx.webflux.reactive.repositories;

import com.william.devx.webflux.reactive.domain.Restaurant;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Created by sungang on 2018/4/4.
 */
public interface RestaurantRepository extends ReactiveCrudRepository<Restaurant, String> {
}
