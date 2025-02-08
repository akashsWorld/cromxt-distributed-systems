package com.cromxt.common.requests.service;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class BucketObject {
    private String bucketId;
    private String hostName;
    private Integer rpcPort;
    private Integer httpPort;
}
