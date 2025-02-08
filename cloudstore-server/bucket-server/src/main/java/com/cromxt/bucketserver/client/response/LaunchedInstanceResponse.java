package com.cromxt.bucketserver.client.response;

public record LaunchedInstanceResponse(
   Integer rpcPort,
   Integer httpPort
){
}
