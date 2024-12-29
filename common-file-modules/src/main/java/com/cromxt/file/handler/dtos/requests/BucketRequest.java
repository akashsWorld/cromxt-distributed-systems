package com.cromxt.file.handler.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BucketRequest{
    private String id;
    private String hostname;
    private Integer port;
}
