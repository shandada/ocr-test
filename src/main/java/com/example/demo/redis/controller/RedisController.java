package com.example.demo.redis.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.redis.pojo.TFileInfo;
import com.example.demo.redis.service.TFileInfoService;
import com.example.demo.redis.vo.OneChapter;
import com.example.demo.redis.vo.Result;
import com.example.demo.redis.vo.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/22 15:04
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TFileInfoService tFileInfoService;

    @GetMapping("/list")
    public Result findAll() {
        long start = System.currentTimeMillis();
        //先到redis中取.
        String fileList = redisTemplate.opsForValue().get("results");
        //如果存在 取出 返回
        if (fileList != null && !fileList.equals("")) {
            ResultData resultData = new ResultData();
            resultData.setResults(fileList);
            return Result.okDataes(resultData);
        }
        //不存在 存放redis中 再取
        List<TFileInfo> fileInfos = tFileInfoService.list(null);
        System.out.println("fileInfos = " + fileInfos);
        //redis中获取
        redisTemplate.opsForValue().set("results", JSON.toJSONString(fileInfos));

        //规范输出
        ResultData resultData = new ResultData();
        resultData.setResults(fileInfos);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return Result.okDataes(resultData);
    }

    @PostMapping("/add")
    public Result add(TFileInfo fileInfo) {
        for (int i = 0; i < 10000; i++) {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            TFileInfo tFileInfo = new TFileInfo(
                    //文件id  //文件名   //版本号 供应商id 名称          描述       创建时间       更新时间         类型   大小  ceph_key    标签       //上传人   //逻辑删除,0 显示
                    id, "文件夹", new Date().toString(), id, "CRLF", "经典", null,null, "type", "sizes", "file_key", null, "user", 0);

            tFileInfoService.save(tFileInfo);
        }
        return Result.ok();
    }


    /**
     * 树状目录
     *
     * @return
     */
    @ApiOperation(value = "树状目录", notes = "应用本体")
    @GetMapping("/treeAll")
    public Result getChapterList() {
        List<OneChapter> list = tFileInfoService.queryChapterAndVideoList();
        ResultData resultData = new ResultData();
        resultData.setResults(list);
        return Result.okData(resultData);
    }

}
