package com.cromxt.bucket.dtos;


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
    private Long availableSpaceInBytes;
}
