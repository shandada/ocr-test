package com.example.demo.test;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.WebParam;

@RestController
@RequestMapping("/SpringBoot")
public class Test01 {

    @GetMapping("")
    public String test(){
        return "SpringBoot";
    }
}
