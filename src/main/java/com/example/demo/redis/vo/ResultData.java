package com.example.demo.redis.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultData{
    private Object result;
    //返回多条数据
    private Object results;
    //总共多少条
    private Long total;
//    //当前页数
    private long current;

}
