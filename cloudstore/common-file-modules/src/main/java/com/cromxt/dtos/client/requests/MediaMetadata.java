package com.cromxt.dtos.client.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MediaMetadata {
    Long fileSize;
    String fileExtension;
}
