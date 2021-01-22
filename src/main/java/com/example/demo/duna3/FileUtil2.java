package com.example.demo.duna3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * // 文件下载工具类
 *
 * @author Administrator
 */
public class FileUtil2 {
    private static final String encoding = "utf-8";

//    public static void download2(String filenameUrl, HttpServletResponse res) throws IOException {
    public static void download2() throws IOException {
        File file = new File("E:\\file-supplier-1.0.jar");
        String name = file.getName();
        long length = file.length();
        System.out.println("name = " + name);
//        bis = new BufferedInputStream(new FileInputStream(new File("E:\\\\file-supplier-1.0.jar\"")));
        DownloadTask downloadManager = new DownloadTask();
        downloadManager.setSleepSeconds(5);

        String downladFileName = downloadManager.download(name, encoding,length);
        System.out.println("Download file is " + downladFileName);

//        //获取session对象
//        String name = (String) request.getSession().getAttribute("handleName");
//        //根据路径,切割获取文件后缀名
//        String loadType = (String) request.getSession().getAttribute("handleUrl");
//        System.out.println(loadType);
//        String NewType = loadType.substring(loadType.lastIndexOf("."));
//        System.out.println("NewType: " + NewType);
//        System.out.println(name);
//
//        while (i != -1) {
//            outputStream.write(buff, 0, buff.length);
//            //name 文件名, NewType .文件类型 例如: .docx  .pdf .png
//            res.setHeader("Content-Disposition", "attachment;filename=" + new String((name + NewType).getBytes("utf-8"), "iso-8859-1"));
//            outputStream.flush();
//            i = bis.read(buff);
//        }

    }
}
