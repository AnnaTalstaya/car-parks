package com.talstaya.carparks.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cities")
@Data
@Builder
public class City {
    @Id
    private String id;
    private String name;
    private String country;
}
