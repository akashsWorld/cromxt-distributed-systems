package com.cromxt.cloudstore.service.impl;

import com.cromxt.cloudstore.clients.GatewayClient;
import com.cromxt.cloudstore.repository.BucketRepository;
import com.cromxt.cloudstore.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final BucketRepository bucketRepository;
    private final GatewayClient gatewayClient;

}
