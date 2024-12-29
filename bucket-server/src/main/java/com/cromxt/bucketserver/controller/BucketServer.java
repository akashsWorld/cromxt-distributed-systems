package com.cromxt.bucketserver.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BucketServer {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
