package com.cromxt.dtos.service;

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
    public List<BucketObject> newBucketObjects;
}
