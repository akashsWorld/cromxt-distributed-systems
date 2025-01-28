package com.cromxt.bucketserver.models;


import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Buckets {

    @MongoId
    private String id;
    @Indexed(unique = true)
    private String hostname;
    private Integer port;
}
