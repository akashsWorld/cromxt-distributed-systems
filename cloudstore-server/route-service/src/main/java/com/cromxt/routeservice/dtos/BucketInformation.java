package com.cromxt.routeservice.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketInformation {
    private String bucketId;
    private Long availableSpaceInBytes;
}
