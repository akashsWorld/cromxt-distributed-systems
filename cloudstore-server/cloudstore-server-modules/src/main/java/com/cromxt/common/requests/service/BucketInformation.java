package com.cromxt.common.requests.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




//This class is used to generate bucket information on the kafka.
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BucketInformation {
    private String bucketId;
    private Long availableSpaceInBytes;
}
