package com.cromxt.cloudstore.service.impl;


import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.clients.RouteServiceClient;
import com.cromxt.cloudstore.dtos.MediaObjectMetadata;
import com.cromxt.cloudstore.dtos.requests.MediaUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import com.cromxt.cloudstore.entity.MediaObjects;
import com.cromxt.cloudstore.repository.MediaRepository;
import com.cromxt.cloudstore.service.MediaService;
import com.cromxt.dtos.client.requests.MediaMetadata;
import com.cromxt.dtos.client.response.BucketDetails;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MediaServiceImpl implements MediaService {

    private final BucketClient bucketClient;
    private final RouteServiceClient routeService;
    private final MediaRepository mediaRepository;

    public MediaServiceImpl(BucketClient bucketClient,
                            RouteServiceClient routeService,
                            MediaRepository mediaRepository) {
        this.bucketClient = bucketClient;
        this.routeService = routeService;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Mono<FileResponse> saveFile(
            FilePart file,
            String preferredFileName,
            Boolean hlsStatus,
            Long fileSize
    ) {
        String fileExtension = getFileExtension(file.filename());


        MediaMetadata fileMetaData = new MediaMetadata(fileSize, fileExtension);
        // TODO: Enable the route service after later.

        // Mono<BucketAddress> bucketDetails = routeService.getBucketAddress(fileMetaData);

        Mono<BucketDetails> bucketDetails = Mono.just(new BucketDetails("bucket-1", "192.168.0.146", 9090, 9091));


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
                        .bucketId(null)
                        .build();
                return mediaRepository.save(mediaObjects)
                        .map(savedObject -> new FileResponse(
                                accessUrlBuilder(savedObject.getBucketId(), savedObject.getId())
                        ));
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


    private String accessUrlBuilder(String bucketId, String fileId) {
//       TODO:Implement the media url pattern later according to design.
        return String.format("https://%s-%s", bucketId, fileId);
    }


}