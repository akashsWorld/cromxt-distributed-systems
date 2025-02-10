package com.cromxt.common.server.requests.client.response.derived;

import com.cromxt.common.server.requests.client.response.BucketResponse;
import com.cromxt.common.server.requests.client.response.BucketResponseDTO;
import com.cromxt.common.server.requests.client.response.ResponseState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewBucketListResponse extends BucketResponse {
    private List<BucketResponseDTO> buckets;

    public NewBucketListResponse(ResponseState status, List<BucketResponseDTO> buckets) {
        super(status);
        this.buckets = buckets;
    }
}
