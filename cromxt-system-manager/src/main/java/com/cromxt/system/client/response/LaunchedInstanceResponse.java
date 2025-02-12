package com.cromxt.system.client.response;

public record LaunchedInstanceResponse(
   Integer rpcPort,
   Integer httpPort
){
}
