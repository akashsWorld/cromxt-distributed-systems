package com.cromxt.bucket.service;

import com.cromxt.files.proto.HLSStatus;

import java.nio.file.Path;

public interface FileService {

    String createFilePath(String contentType);
}

