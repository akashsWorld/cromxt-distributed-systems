package com.cromxt.mediaserver.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "media_objects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Medias {

    @Id
    private String id;
    @Indexed(unique = true)
    private String mediaId;
    private String userId;
    private Long size;
    private String bucketId;
    private String fileExtension;
    private MediaStatus mediaStatus;
}
