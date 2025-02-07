package com.cromxt.cloudstore.service.impl;


import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.clients.RouteServiceClient;
import com.cromxt.cloudstore.dtos.MediaObjectMetadata;
import com.cromxt.cloudstore.entity.MediaObjects;
import com.cromxt.cloudstore.repository.MediaRepository;
import com.cromxt.cloudstore.service.MediaService;
import com.cromxt.dtos.client.response.BucketDetails;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MediaServiceImpl implements MediaService {

    private final BucketClient bucketClient;
    private final RouteServiceClient routeService;
    private final MediaRepository mediaRepository;
    private final String protocol;

    public MediaServiceImpl(BucketClient bucketClient, RouteServiceClient routeService, MediaRepository mediaRepository, Environment environment) {
        this.bucketClient = bucketClient;
        this.routeService = routeService;
        this.mediaRepository = mediaRepository;
        Boolean isSsl = environment.getProperty("CLOUD_STORE_CONFIG_IS_SSL", Boolean.class);
        this.protocol = Boolean.TRUE.equals(isSsl) ? "https" : "http";
    }

    @Override
    public Mono<String> saveFile(
            FilePart file,
            String preferredFileName,
            Boolean hlsStatus,
            Long fileSize
    ) {
        String fileExtension = getFileExtension(file.filename());

        Mono<BucketDetails> bucketDetails = Mono.just(new BucketDetails("bucket-1", "192.168.0.181", 9090, 9091));


        return bucketDetails.flatMap(bucket -> {
            MediaObjectMetadata mediaObjectMetadata =
                    MediaObjectMetadata.builder()
                            .contentLength(fileSize)
                            .fileName(preferredFileName)
                            .contentType(fileExtension)
                            .hlsStatus(hlsStatus)
                            .build();
            Mono<String> mediaObjectDetailsMono = bucketClient.uploadFile(file.content(), mediaObjectMetadata, bucket);


            return mediaObjectDetailsMono.flatMap(fileName -> {
                MediaObjects mediaObjects = MediaObjects.builder()
                        .fileExtension(fileExtension)
                        .name(fileName)
                        .size(fileSize)
                        .bucketId(bucket.bucketId())
                        .build();
                return mediaRepository.save(mediaObjects)
                        .map(savedObject ->
                                accessUrlBuilder(bucket, fileName, fileExtension)
                        );
            });

        });

    }

    private String getFileExtension(String fileName) {
        String fileExtension = fileName
                .substring(fileName
                        .lastIndexOf(".") + 1
                );
        if (fileExtension.isEmpty()) {
            throw new RuntimeException("File extension is empty");
        }
        return fileExtension;
    }

    private String accessUrlBuilder(BucketDetails bucketDetails, String fileName, String extension) {

        return String.format("%s://%s:%s/api/v1/objects/%s.%s",
                protocol,
                bucketDetails.hostName(),
                bucketDetails.httpPort(),
                fileName,
                extension);
    }


}