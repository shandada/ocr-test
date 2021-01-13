package com;


/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/4 17:13
 */
public class BusConfDeviceType {
    private String id;

    /**
    * 数据有效 0 已删除 1
    */
    private Integer status;

    /**
    * 父节点
    */
    private String pid;

    /**
    * 设备类型
    */
    private String deviceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}