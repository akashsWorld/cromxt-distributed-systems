package com.cromxt.crom_bucket.service.impl;

import com.cromxt.crom_bucket.dtos.request.MediaRequestDTO;
import com.cromxt.crom_bucket.dtos.response.MediaRegisterResponse;
import com.cromxt.crom_bucket.service.FileService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class FileServiceImpl implements FileService {

    private final String SYSTEM_ABSOLUTE_PATH;
    private static final String ROOT_FOLDER = "/root_bucket";

    public FileServiceImpl(Environment environment) {
        this.SYSTEM_ABSOLUTE_PATH = environment.getProperty("BUCKET_SERVICE.SYSTEM_ABSOLUTE_PATH")+"/root_bucket";
    }


    @Override
    public Mono<MediaRegisterResponse> saveFile(MediaRequestDTO mediaRequestDTO) {
        return null;
    }
}
