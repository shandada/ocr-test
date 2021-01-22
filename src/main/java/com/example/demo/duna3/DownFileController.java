package com.example.demo.duna3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/18 13:39
 */
@RestController
@RequestMapping("/test")
public class DownFileController {

    @GetMapping(value = "/download")
    public void download() throws IOException {
        FileUtil2.download2();
    }
}
