package com.cromxt.common.server.requests.client.response.derived;


import com.cromxt.common.server.requests.client.response.BucketResponse;
import com.cromxt.common.server.requests.client.response.ResponseState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends BucketResponse {
        private String message;
        public ErrorResponse(String message,ResponseState status) {
                super(status);
                this.message = message;
        }
}
