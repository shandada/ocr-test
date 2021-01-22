package com.example.demo.file;

import java.io.File;
import java.util.List;

/**
 * @Description:
 *
 *
 *    /**
 *      * 删除某个文件或者是某个目录
 *      * @param file 文件路径或者是文件夹路径
 * @Auther: Shan PengKun
 * @Date: 2021/1/15 10:02
 */
public class DelFile {
    public static void main(String[] args) {

        File file = new File("D:\\p");
        if (file.isDirectory()) {
            // 获取文件夹下所有子文件夹
            String[] children = file.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                new File(file, children[i]);
            }
        }
        // 目录空了，进行删除
        file.delete();
    }
}
