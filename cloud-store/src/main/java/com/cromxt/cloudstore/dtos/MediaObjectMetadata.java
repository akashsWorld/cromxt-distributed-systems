package com.cromxt.cloudstore.dtos;

import com.cromxt.files.proto.HLSStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MediaObjectMetadata{
    private String contentType;
    private HLSStatus hlsStatus;
}