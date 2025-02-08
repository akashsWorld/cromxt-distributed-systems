package com.cromxt.mediaserver.dtos;

import com.cromxt.mediaserver.entity.MediaStatus;

public record NewMediaRequest(
        String userId,
        String bucketId,
        String fileExtension,
        String mediaId,
        MediaStatus mediaStatus
) {
}
