package com.cromxt.bucket.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Service
public class ApplicationHostNetworkFinder {


    private static Boolean APPLICATION_HOSTNAME_AUTO_DISCOVERY;
    private static Integer APPLICATION_PORT;


    public ApplicationHostNetworkFinder(
            Environment environment
    ) {
        APPLICATION_HOSTNAME_AUTO_DISCOVERY = environment.getProperty("BUCKET_HOST_NAME_AUTO_DISCOVERY", Boolean.class, false);
        APPLICATION_PORT = environment.getProperty("server.port", Integer.class, 8090);
    }

    public String getApplicationHostname() {
        String hostName;
        if(APPLICATION_HOSTNAME_AUTO_DISCOVERY) {
            try {
                InetAddress ip = InetAddress.getLocalHost();
                hostName = ip.getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
                hostName = "localhost";
                log.warn("Bucket starts with {}", "localhost");
            }
        }else{
            hostName = "localhost";
        }
        return hostName;
    }
    public Integer getApplicationPort() {
        return APPLICATION_PORT;
    }
}
