package com.cromxt.cloudstore.service.impl;


import com.cromxt.cloudstore.clients.BucketClient;
import com.cromxt.cloudstore.clients.RouteServiceClient;
import com.cromxt.cloudstore.dtos.MediaObjectMetadata;
import com.cromxt.cloudstore.dtos.requests.FileUploadRequest;
import com.cromxt.cloudstore.dtos.response.FileResponse;
import com.cromxt.cloudstore.dtos.response.MediaObjectDetails;
import com.cromxt.cloudstore.entity.MediaObjects;
import com.cromxt.cloudstore.repository.MediaRepository;
import com.cromxt.cloudstore.service.FileService;
import com.cromxt.dtos.client.requests.FileMetaData;
import com.cromxt.dtos.client.response.BucketAddress;
import com.cromxt.files.proto.HLSStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileServiceImpl implements FileService {

    private final BucketClient bucketClient;
    private final RouteServiceClient routeService;
    private final MediaRepository mediaRepository;

    public FileServiceImpl(BucketClient bucketClient,
                           RouteServiceClient routeService,
                           MediaRepository mediaRepository) {
        this.bucketClient = bucketClient;
        this.routeService = routeService;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Mono<FileResponse> saveFile(FileUploadRequest fileUploadRequest) {
        FilePart mediaObject = fileUploadRequest.mediaObject();
        String fileExtension = getFileExtension(mediaObject.filename());

        Mono<Long> fileSize = getFileSize(mediaObject);

        return fileSize.flatMap(totalFileSize -> {
            FileMetaData fileMetaData = new FileMetaData(totalFileSize, fileExtension);
            Mono<BucketAddress> bucketDetails = routeService.getBucketId(fileMetaData);


            return bucketDetails.flatMap(bucket -> {
                MediaObjectMetadata mediaObjectMetadata =
                        MediaObjectMetadata.builder()
                                .hlsStatus(HLSStatus.ENABLED)
                                .build();
                Mono<MediaObjectDetails> mediaObjectDetailsMono = bucketClient.uploadFile(mediaObject.content(), mediaObjectMetadata, bucket);


                return mediaObjectDetailsMono.flatMap(mediaObjectDetails -> {
                    MediaObjects mediaObjects = MediaObjects.builder()
                            .fileExtension(fileExtension)
                            .name(mediaObjectDetails.getFileId())
                            .size(totalFileSize)
                            .bucketId(null)
                            .build();
                    return mediaRepository.save(mediaObjects)
                            .map(savedObject -> new FileResponse(
                                    accessUrlBuilder(savedObject.getBucketId(), savedObject.getId())
                            ));

                });

            });

        });
    }

    private String getFileExtension(String fileName) {
        return fileName
                .substring(fileName
                        .lastIndexOf(".")
                );
    }

    private Mono<Long> getFileSize(FilePart mediaObject) {
        return mediaObject
                .content()
                .map(dataBuffer -> {
                    long size = dataBuffer.readableByteCount();
                    dataBuffer.readPosition(0);
                    return size;
                })
                .reduce(Long::sum);
    }

    private String accessUrlBuilder(String bucketId, String fileId) {
//       TODO:Implement the media url pattern later according to design.
        return String.format("https://%s-%s", bucketId, fileId);
    }


}
