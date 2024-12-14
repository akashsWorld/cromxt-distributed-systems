package com.cromxt.file.handler.dtos.response;


public record ErrorResponse (
        String message,
        String reason
){
}
