package com.example.demo.io;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/9/28  10:51
 */
public class test01 {
    public static void main(String[] args) {
        String s = "[{\"words\":\"本项目第-阶段以《中健药业业\\u2019中台需求分析报告》为蓝本界定,主要涵盖业务主数据(产品,客户,\"},{\"words\":\"价格,组织架构,代理商、CSO等),经销商管理(开户,审批,资信管理等),采购管理(计划,合同\"},{\"words\":\"采购订单),销售管理(B2B下单,审批,退换货,发票、物流等),经销商返利(政策协议,返利计算\"},{\"words\":\"返利核销等)管理。同时还包括与中健NC系统、各家工业ERP系统的对接。\"},{\"words\":\"在使用用户范围上,主要包括公司内部管理层、业务人员、销售管理人员、质量管控人员、采购负责人员\"},{\"words\":\"以及各工业驻场代表,同时还包括部分外部代理商用户\"},{\"words\":\"统的呈现形式包括电脑端的网页管理平台和移动端的APP平台两类,两类平台采用同样的用户名密码进\"},{\"words\":\"行登录,有统的权限管控中心\"}]";
        JSONArray objects = JSONArray.parseArray(s);
        String a="";
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            String string = jsonObject.getString("words");
            list.add(string);
            a+=string;
            System.out.println(string);
        }

        System.out.println("a:  "+a);
        String x ="a.jpg";
        x.substring(0,x.lastIndexOf("."));
    }
}
