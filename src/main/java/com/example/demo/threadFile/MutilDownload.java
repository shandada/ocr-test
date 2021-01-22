package com.example.demo.threadFile;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MutilDownload {
    private static String path = "D:\\a.docx";
    private static final int threadCount = 3;

    public static void main(String[] args) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                int contentLength = conn.getContentLength();
                System.out.println("length" + contentLength);

//                1、对象声明：RandomAccessFile raf = newRandomAccessFile(File file, String mode);
//                其中参数 mode 的值可选 "r"：可读，"w" ：可写，"rw"：可读性；
                RandomAccessFile rafAccessFile = new RandomAccessFile("x.docx", "rw");
                rafAccessFile.setLength(contentLength);
                int blockSize = contentLength / threadCount;
                for (int i = 0; i < threadCount; i++) {
                    int startIndex = i * blockSize; //每个现成下载的开始位置
                    int endIndex = (i + 1) * blockSize - 1;// 每个线程的结束位置
                    if (i == threadCount - 1) {
                        //最后一个线程
                        endIndex = contentLength - 1;
                    }

                    new DownloadThread(startIndex, endIndex, i).start();
                }

            }
        } catch (Exception e) {

        }
    }

    private static class DownloadThread extends Thread {
        private int startIndex;
        private int endIndex;
        private int threadId;

        public DownloadThread(int startIndex, int endIndex, int threadId) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex); //固定写法，请求部分资源
                int responseCode = conn.getResponseCode();  // 206表示请求部分资源
                if (responseCode == 206) {
                    RandomAccessFile rafAccessFile = new RandomAccessFile("x.docx", "rw");
                    rafAccessFile.seek(startIndex);
                    InputStream is = conn.getInputStream();
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        rafAccessFile.write(buffer, 0, len);
                    }
                    rafAccessFile.close();

                    System.out.println("线程" + threadId + "下载完成");
                }
            } catch (Exception e) {

            }
        }
    }
}