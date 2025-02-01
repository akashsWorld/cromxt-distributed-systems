package com.cromxt.cloudstore.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MediaObjectDetails {
    String fileId;
    String bucketId;
    String fileSize;
    String fileExtension;
}
