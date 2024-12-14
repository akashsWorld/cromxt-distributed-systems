package com.cromxt.file_handler.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collation = "media_objects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MediaObjects {

    @Id
    private String id;
    private String name;
    private String size;
    private String path;
    @DocumentReference
    private Bucket bucket;
}
