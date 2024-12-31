package com.cromxt.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BucketsUpdateRequest {
    public Method method;
    public List<BucketObjects> bucketObjects;
}
