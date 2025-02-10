package com.cromxt.common.dtos.mediaserver.requests;




public record NewMediaRequest(
        String userId,
        String bucketId,
        String fileExtension,
        String mediaId,
        MediaStatus mediaStatus
) {
}
