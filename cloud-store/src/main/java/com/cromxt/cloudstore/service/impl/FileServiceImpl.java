package com.cromxt.cloudstore.service.impl;


import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.dtos.requests.FileUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import com.cromxt.cloudstore.service.FileService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileServiceImpl implements FileService {

    private final BucketClient routeServiceClient;

    public FileServiceImpl(@Qualifier("bucketGRPCClient") BucketClient routeServiceClient) {
        this.routeServiceClient = routeServiceClient;
    }

    @Override
    public Mono<FileResponse> saveFile(FileUploadRequest fileUploadRequest) {

//        Implement the functionality of saving the file and store it in the database.

        return Mono.empty();
    }


}
