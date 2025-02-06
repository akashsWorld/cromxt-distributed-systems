package com.cromxt.bucket.service;


import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;
import com.cromxt.proto.files.MediaMetaData;

public interface FileService {
    FileDetails generateFileDetails(MediaMetaData contentType);
    String getFileAbsolutePath(String fileName);

}

