package com.cromxt.common.server.requests.client.response.derived;


import com.cromxt.common.server.requests.client.response.BucketResponseDTO;
import com.cromxt.common.server.requests.client.response.BucketResponse;
import com.cromxt.common.server.requests.client.response.ResponseState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewBucketResponse extends BucketResponse {
        private BucketResponseDTO bucket;
        public NewBucketResponse(ResponseState status,
                                 BucketResponseDTO bucket) {
                super(status);
                this.bucket = bucket;
        }
}
