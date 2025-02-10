package com.cromxt.common.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BucketUpdateRequest {
    public Method method;
    public BucketObject newBucketObject;
}
