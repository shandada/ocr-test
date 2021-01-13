package com.example.demo.spit;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/12/31 18:08
 */

public class Demo {
    public static void main(String[] args) {

        String s1 = "localhost:8089.fileName.download/a.fsvgvfgfvdv";

        String substring = s1.substring(s1.lastIndexOf("."));
        System.out.println(substring);

        String s2 = "localhost:8089.fileName.download/a.docx";
        String[] split = s2.split("\\.");
        String ss = split[split.length - 1];
        System.out.println(ss);
    }
}
