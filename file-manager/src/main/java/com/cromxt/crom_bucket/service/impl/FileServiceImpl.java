package com.cromxt.crom_bucket.service.impl;

import com.cromxt.crom_bucket.dtos.request.MediaUploadRequest;
import com.cromxt.crom_bucket.dtos.response.MediaResponse;
import com.cromxt.crom_bucket.service.FileService;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {

    private final String SYSTEM_ABSOLUTE_PATH;


    public FileServiceImpl(Environment environment) {
        this.SYSTEM_ABSOLUTE_PATH = environment.getProperty("BUCKET_SERVICE.SYSTEM_ABSOLUTE_PATH")+"/root_bucket";
    }


    @Override
    public Mono<MediaResponse> saveFile(MediaUploadRequest mediaUploadRequest) {
        FilePart media = mediaUploadRequest.media();
        String fileExtension = media.filename().substring(media.filename().lastIndexOf("."));
        String fileName = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes())+fileExtension;
        return media.transferTo(Paths.get(SYSTEM_ABSOLUTE_PATH+"/"+fileName))
                .then(Mono.just(new MediaResponse("http://localhost:9502"+"/"+fileName)))
                .onErrorResume(Mono::error);
    }

    @Override
    public Mono<MediaResponse> deleteFile(String fileName) {
        Path filePath = Paths.get(SYSTEM_ABSOLUTE_PATH+"/"+fileName);
        return Mono.fromCallable(() -> {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return new MediaResponse("http://localhost:9502"+"/"+fileName);
            } else {
                return new MediaResponse("File not found");
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
