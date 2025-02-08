package com.cromxt.toolkit.cloudstore.metadata;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class MediaObjectMetaData{
    private String contentType;
    private Long contentLength;
    private Boolean hlsStatus;
}
