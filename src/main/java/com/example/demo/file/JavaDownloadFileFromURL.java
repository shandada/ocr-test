package com.example.demo.file;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class JavaDownloadFileFromURL {

    public static void main(String[] args) {
        String url = "http://192.168.1.37:7480/nanjing/400ceda06e054e119d48f63f14c3436a-%E9%98%BF%E9%87%8C%E5%A4%A7%E9%B1%BC.pdf?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20210126T034325Z&X-Amz-SignedHeaders=host&X-Amz-Expires=900&X-Amz-Credential=UT8AMGN23LVOJBTNMYP6%2F20210126%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=18662693b450f0df60e60a19ed8d0f0d8105a4dd52e08ea45befceb1c1c534a8";
//        String url = "https://www.cnblogs.com/cnsyear/p/13229686.html";

        try {
            downloadUsingNIO(url, "D:\\xv.docx");

            downloadUsingStream(url, "D:\\x.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadUsingStream(String urlStr, String file) throws IOException{
        java.net.URL url = new java.net.URL(urlStr);

        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
        String contentType = urlConn.getContentType();
        System.out.println("contentType:" + contentType);


        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);

        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
        String contentType = urlConn.getContentType();
        System.out.println("contentType:" + contentType);

        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

}

