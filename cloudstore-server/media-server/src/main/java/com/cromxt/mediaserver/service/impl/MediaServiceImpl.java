package com.cromxt.mediaserver.service.impl;


import com.cromxt.mediaserver.dtos.NewMediaRequest;
import com.cromxt.mediaserver.dtos.UpdateMediaRequest;
import com.cromxt.mediaserver.entity.Medias;
import com.cromxt.mediaserver.repository.MediaRepository;
import com.cromxt.mediaserver.service.MediaClientService;
import com.cromxt.mediaserver.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService, MediaClientService {

    private final MediaRepository mediaRepository;
    private final String protocol;



    @Override
    public Mono<String> createMedia(NewMediaRequest mediaRequest) {
        return mediaRepository.save(Medias.builder()
                        .bucketId(mediaRequest.bucketId())
                        .fileExtension(mediaRequest.fileExtension())
                        .mediaId(mediaRequest.mediaId())
                        .userId(mediaRequest.userId())
                        .mediaStatus(mediaRequest.mediaStatus())
                        .build())
                .map(Medias::getId);
    }

    @Override
    public Mono<Void> updateMedia(
            String mediaId,
            UpdateMediaRequest mediaRequest){
        return mediaRepository.findById(mediaId).flatMap(savedMedia->{
            savedMedia.setMediaStatus(mediaRequest.mediaStatus());
            savedMedia.setSize(mediaRequest.size());
            return mediaRepository.save(savedMedia).then();
        });

    }
}