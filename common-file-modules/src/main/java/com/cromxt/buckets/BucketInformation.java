package com.cromxt.buckets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BucketInformation {
    private String bucketId;
    private Long availableSpaceInBytes;
}
