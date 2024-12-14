package com.cromxt.file_handler.controller;

import com.cromxt.file_handler.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    public Mono<Void> addBucket(){
        return null;
    }


}
