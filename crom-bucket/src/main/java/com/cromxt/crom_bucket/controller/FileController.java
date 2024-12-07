package com.cromxt.crom_bucket.controller;

import com.cromxt.crom_bucket.dtos.request.MediaRequestDTO;
import com.cromxt.crom_bucket.dtos.response.MediaResponse;
import com.cromxt.crom_bucket.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<MediaResponse>> uploadFile(@ModelAttribute MediaRequestDTO mediaRequestDTO) {
        return fileService.saveFile(mediaRequestDTO)
                .map(mediaResponse -> new ResponseEntity<>(mediaResponse, HttpStatus.CREATED));
    }

}
