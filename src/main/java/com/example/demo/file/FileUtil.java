package com.example.demo.file;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUtil {

    public static void download(String filename, HttpServletResponse res) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = res.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        // 读取filename    "a.docx"/a.docx

        //
        bis = new BufferedInputStream(new FileInputStream(new File("D:\\work\\fuma\\code\\springboot-test\\fiel\\" +"a.docx")));
        int i = bis.read(buff);
        while (i != -1) {
//            name 文件名, NewType .文件类型 例如: .docx  .pdf .png
//            res.setHeader("Content-Disposition", "attachment;filename=" + new String(("file").getBytes("utf-8"), "iso-8859-1"));

            outputStream.write(buff, 0, buff.length);
            outputStream.flush();
            i = bis.read(buff);
        }
    }

}
