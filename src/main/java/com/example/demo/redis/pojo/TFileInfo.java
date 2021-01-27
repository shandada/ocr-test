package com.example.demo.redis.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Table;
import java.util.Date;


/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 16:19
 */
@Data
@NoArgsConstructor
@ApiModel(value = "文件表基本信息", description = "组织管理")
@Table(name = "t_file_info")
public class TFileInfo {
    /**
     * 文件id
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "文件id")
    @TableId(value = "uid", type = IdType.ASSIGN_UUID)
    private String uid;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date updateTime;


    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "大小")
    private String size;

    @ApiModelProperty(value = "ceph读取key")
    private String fileKey;


    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "上传人用户")
    private String user;


    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField(fill = FieldFill.INSERT, value = "is_delete")
//    @TableLogic(value = "0",delval = "1")
    @TableLogic
    private Integer isDelete;
    //不对应数据库字段
//    @TableField(exist = false)
//    private List<TFileInfo> children;

    public TFileInfo(String uid, String fileName, String version, String supplierId, String supplierName, String remark, Date createTime, Date updateTime, String type, String size, String fileKey, String tag, String user, Integer isDelete) {
        this.uid = uid;
        this.fileName = fileName;
        this.version = version;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.type = type;
        this.size = size;
        this.fileKey = fileKey;
        this.tag = tag;
        this.isDelete = isDelete;
        this.user = user;
    }
}