package com.cromxt.toolkit.cloudstore;

import java.io.InputStream;

public interface CloudStoreClient {

    String saveFile(Long contentLength, InputStream data);
}
