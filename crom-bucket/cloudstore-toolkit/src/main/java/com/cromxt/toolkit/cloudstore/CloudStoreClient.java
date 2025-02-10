package com.cromxt.toolkit.cloudstore;

import com.cromxt.toolkit.cloudstore.response.FileUploadResponse;

import java.io.InputStream;

public interface CloudStoreClient {

    FileUploadResponse saveFile(Long contentLength, InputStream data);
}
