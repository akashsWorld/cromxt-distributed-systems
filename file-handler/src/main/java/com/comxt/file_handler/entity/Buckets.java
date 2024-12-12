package com.comxt.file_handler.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Buckets {

    @MongoId
    private String id;
    private String bucketName;
    private String serverHostName;
    private Integer serverPort;
    private Long availableSpace;
}
