package com.example.demo.stringbuider;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/22 9:54
 */
public class Test01 {

    public static void main(String[] args) {
        String str = "hello";
        str = str + " world";
        System.out.println(str);//打印结果：hello world

        /*String变为StringBuider:利用StringBuider的构造方法或append()方法*/
        StringBuilder sb = new StringBuilder();//调用StringBuider无参数的构造方法
        sb.append("hello").append(" world");//通过.操作符访问append方法
        System.out.println(sb);//打印结果：hello world

        StringBuilder sb1 = new StringBuilder("hello");//调用StringBuider含参数的构造方法
        sb1.append(" world");
        System.out.println(sb1);//打印结果：hello world

        /* StringBuider变为String:调用toString()方法*/
        StringBuilder sb3 = new StringBuilder();
        sb3.append("hello").append(" world");
        String s = sb3.toString();
        System.out.println(s);//打印结果：hello world

        StringBuilder sb4 = new StringBuilder();
        System.out.println(sb4.append("helloWorld"));//拼接字符串
        System.out.println(sb4.reverse());//反转字符串
        System.out.println(sb4.delete(2,6));//删除指定范围的字符
        System.out.println(sb4.insert(2,"你好").insert(3,23));//在指定位置插入可以是任意类型
        System.out.println(sb4.replace(2, 5, "good"));//指定位置替换字符串


    }

}
