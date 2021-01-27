package com.example.demo.redis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@ApiModel(value = "全局统一返回结果")
public class Result {
    @ApiModelProperty(value = "返回码")
    private Integer code;
    private String msg;

    @ApiModelProperty(value = "是否成功")
    private boolean success;

    @ApiModelProperty(value = "返回数据")
    private ResultData data;
    @ApiModelProperty(value = "時間")
    private Long timestamp = System.currentTimeMillis();

    //当前Result不对外公开构造,提供静态方法

    //私有化,不能new 只调用以下方法
    public Result() {

    }

    //返回成功 攜帶數據
    public static Result okDataes(ResultData data) {
        Result r = new Result(); //调用自己私有构造方法
        r.setCode(200);
        r.setSuccess(true);
        r.setMsg("成功");
        r.setData(data);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }
    //返回成功 攜帶數據
    public static Result okData(Object data) {
        Result r = new Result(); //调用自己私有构造方法
        r.setCode(200);
        r.setSuccess(true);
        r.setMsg("成功");
        r.setData(ResultData.builder().result(data).build());
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }

    //返回成功 攜帶數據
//    public static Result okDataRedis(Object data) {
//        Result r = new Result(); //调用自己私有构造方法
//        r.setCode(200);
//        r.setSuccess(true);
//        r.setMsg("成功");
//        r.setData(data);
//        r.setTimestamp(System.currentTimeMillis());
//        return r;
//    }


    //调用成功方法 不攜帶數據
    public static Result ok() {
        Result r = new Result();//调用自己私有构造方法
        r.setSuccess(true);
        r.setCode(200);
        return r;
    }

    //失败调用方法
    public static Result error() {
        Result r = new Result();
        r.setSuccess(false);
        r.setCode(500);
        return r;
    }


}
