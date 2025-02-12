package com.cromxt.system.dtos.response;


import com.cromxt.common.dtos.BaseResponse;
import com.cromxt.common.dtos.CromxtResponseStatus;
import com.cromxt.system.dtos.BucketDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BucketResponse extends BaseResponse {
        private BucketDTO bucket;
        public BucketResponse(CromxtResponseStatus status,
                              BucketDTO bucket) {
                super(status);
                this.bucket = bucket;
        }
}
