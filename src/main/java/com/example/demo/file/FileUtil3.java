package com.example.demo.file;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * // 文件下载工具类
 *
 * @author Administrator
 */
public class FileUtil3 {
    public void download(String uid, HttpServletResponse response) throws IOException {
//            System.out.println("下载文件的id = " + uid);
//            String bucketName = "nanjing";
//            TFileInfo tFileInfo = baseMapper.selectById(uid);
//            String fileKey = tFileInfo.getFileKey();
//            String fileName = tFileInfo.getFileName();
//            System.out.println("下载文件信息: bucketName == uid == fileKey " + bucketName + "==" + uid + "===" + fileKey);
//            InputStream inputStream = CephUtils.readStreamObject(bucketName, fileKey);
//            System.out.println("CEph流: inputStream = " + inputStream);
//            String hehe = new String(fileName.getBytes("utf-8"), "iso-8859-1");
//            response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
//
//
//            byte[] b = new byte[100];
//            int len;
//            while ((len = inputStream.read(b)) > 0) {
//                response.getOutputStream().write(b, 0, len);
//            }
//            inputStream.close();
//            System.out.println("文件下载成功! " + fileName);
//        }
    }
}
