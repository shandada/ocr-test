package com.example.demo.io;

import java.io.File;
import java.io.UnsupportedEncodingException;


/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/9/26 11:54
 */
//图片识别
public class del {
        private static int flag = 1;
    public static void main(String[] args) throws UnsupportedEncodingException {
        //取得这个目录下的所有子文件对象
        String parentPath = "E:\\ocr图像识别\\ax.docx";
        File file = new File(parentPath);//输入要删除文件目录的绝对路径
        file.delete();

    }

}

