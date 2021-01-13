package com.example.demo.file;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

// 文件下载工具类
public class FileUtil2 {
    public static void download(String filenameUrl, HttpServletResponse res, HttpServletRequest request) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = res.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        // 读取filename  url   "a.docx"/a.docx
        System.out.println(filenameUrl);
        bis = new BufferedInputStream(new FileInputStream(new File(filenameUrl)));
        int i = bis.read(buff);

        //获取session对象
        String name = (String) request.getSession().getAttribute("handleName");
        //根据路径,切割获取文件后缀名
        String loadType = (String) request.getSession().getAttribute("handleUrl");
        System.out.println(loadType);
        String NewType = loadType.substring(loadType.lastIndexOf("."));
        System.out.println("NewType: " + NewType);
        System.out.println(name);

        while (i != -1) {
            outputStream.write(buff, 0, buff.length);
            //name 文件名, NewType .文件类型 例如: .docx  .pdf .png
            res.setHeader("Content-Disposition", "attachment;filename=" + new String((name + NewType).getBytes("utf-8"), "iso-8859-1"));
            outputStream.flush();
            i = bis.read(buff);
        }
    }
}
