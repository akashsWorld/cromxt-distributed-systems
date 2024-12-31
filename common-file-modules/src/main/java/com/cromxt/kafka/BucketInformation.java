package com.cromxt.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BucketInformation {
    private String bucketId;
    private String bucketHostName;
    private Integer port;
    private Long availableSpaceInBytes;
}
