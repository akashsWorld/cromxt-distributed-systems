package com.cromxt.toolkit.cloudstore.impl;

import com.cromxt.toolkit.cloudstore.CloudStoreClient;
import com.cromxt.toolkit.cloudstore.response.FileUploadResponse;

import java.io.InputStream;

public class CloudStoreClientImpl implements CloudStoreClient {
    @Override
    public FileUploadResponse saveFile(Long contentLength, InputStream data) {
        return FileUploadResponse.builder().build();
    }
}
