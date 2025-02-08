package com.cromxt.routeservice.controller;

import com.cromxt.routeservice.service.RouteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public record AdminController (
        RouteService routeService
){




}
