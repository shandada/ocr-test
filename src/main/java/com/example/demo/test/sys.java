package com.example.demo.test;

import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/10/13 09:35
 */
public class sys {


    @GetMapping("")
    public static void main(String[] args) {



//        int[] arr1 = {1, 2, 3, 4, 5};
//        int[] arr2 = new int[10];
//        System.arraycopy(arr1, 3, arr2, 8, 2);
//        for (int i = 0; i < arr2.length; i++) {
//            System.out.println(arr2[i]);
//        }
//        System.out.println(UUID.randomUUID().toString());
//        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
        String s = "RabbitMQ.md";

        String[] ss = s.split("\\.");
        System.out.println(ss[0]);
        System.out.println(ss[1]);
        SimpleDateFormat df = new SimpleDateFormat("HH.mm.ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

        System.err.println("s"+"\n"+"s2");

    }
}
