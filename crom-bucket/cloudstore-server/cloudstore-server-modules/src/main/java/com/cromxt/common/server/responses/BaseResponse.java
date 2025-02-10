package com.cromxt.common.server.responses;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class BaseResponse {
    private ResponseStatus responseStatus;
    private String message;
}
