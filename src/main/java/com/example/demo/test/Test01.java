package com.example.demo.test;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/SpringBoot")
public class Test01 {
    @Resource
    HttpSession session;
    @GetMapping("")
    public String test() {
        String s = "name";
        //存
        session.setAttribute("name", s);
        //取
        Object name = session.getAttribute("name");
        System.out.println(name);
        return (String) name;
    }
}
