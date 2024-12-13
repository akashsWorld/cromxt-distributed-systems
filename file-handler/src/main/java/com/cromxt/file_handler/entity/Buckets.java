package com.cromxt.file_handler.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Buckets {
    @Id
    private String id;
    private String hostname;
    private String port;
}
