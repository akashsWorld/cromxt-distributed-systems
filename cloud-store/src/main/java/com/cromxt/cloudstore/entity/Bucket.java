package com.cromxt.cloudstore.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Bucket {
    @Id
    private String id;
    private String hostname;
    private Integer port;
}
