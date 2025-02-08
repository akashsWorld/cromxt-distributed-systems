package com.cromxt.mediaserver.dtos;

import com.cromxt.mediaserver.entity.MediaStatus;

public record UpdateMediaRequest(
        Long size,
        MediaStatus mediaStatus
) {
}
