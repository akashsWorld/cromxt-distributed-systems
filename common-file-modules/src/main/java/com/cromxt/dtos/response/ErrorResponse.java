package com.cromxt.dtos.response;


public record ErrorResponse (
        String message,
        String reason
){
}
