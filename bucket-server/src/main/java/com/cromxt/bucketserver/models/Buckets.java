package com.cromxt.bucketserver.models;


import lombok.*;
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
    private String hostname;
    private Integer port;
}
