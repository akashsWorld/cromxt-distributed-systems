package com.cromxt.cloudstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cromxt.proto.files.HLSStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MediaObjectMetadata{
    private String contentType;
    private HLSStatus hlsStatus;
}