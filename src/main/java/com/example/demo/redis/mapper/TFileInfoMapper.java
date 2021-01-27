package com.example.demo.redis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.redis.pojo.TFileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 16:19
 */
@Mapper
public interface TFileInfoMapper extends BaseMapper<TFileInfo> {

    @Select("SELECT * FROM t_file_info WHERE supplier_id IN(SELECT gid FROM t_supplier WHERE gid= #{gid, jdbcType=VARCHAR})")
    @Results({
            @Result(column = "uid", property = "uid"),
            @Result(column = "fileName", property = "fileName"),
            @Result(column = "supplierId", property = "supplier_id"),
            @Result(column = "supplierName", property = "supplier_name"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "createTime", property = "create_time"),
            @Result(column = "updateTime", property = "update_time"),
            @Result(column = "fileKey", property = "file_key"),
            @Result(column = "isDelete", property = "is_delete"),
    })
    public List<TFileInfo> supplierId(@Param("gid") String gid);

}