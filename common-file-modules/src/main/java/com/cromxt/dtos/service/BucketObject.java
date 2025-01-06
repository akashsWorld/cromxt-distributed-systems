package com.cromxt.dtos.service;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class BucketObject {
    private String bucketId;
    private String hostName;
    private Integer port;
}
