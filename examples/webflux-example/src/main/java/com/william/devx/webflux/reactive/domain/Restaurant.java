package com.william.devx.webflux.reactive.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * Created by sungang on 2018/4/4.
 */
@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Restaurant {

    @Id
    public String id;
    @NonNull
    private String name;
    @NonNull
    private String address;
    @NonNull
    private String telephone;
}
