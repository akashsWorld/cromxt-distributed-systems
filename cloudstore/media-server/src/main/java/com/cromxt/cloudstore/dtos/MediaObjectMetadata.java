package com.cromxt.cloudstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MediaObjectMetadata{
    private String contentType;
    private String fileName;
    private Boolean hlsStatus;
    private Long contentLength;
}