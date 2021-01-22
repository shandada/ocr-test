package com.example.demo.duna3;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.*;

public class DownloadTask {
    // 分段下载的线程个数
    private int threadNum = 5;
    private URL url = null;
    private long threadLength = 0;
    // 目标文件路径与名字
    public String fileDir = "D:\\";
    public String fileName = "test.html";
    public boolean statusError = false;
    private String charset;

    public long sleepSeconds = 5;

    public String download(String urlStr, String charset, long length) {
        statusError = false;
        this.charset = charset;
        long contentLength = 0;
        CountDownLatch latch = new CountDownLatch(threadNum);
        ChildThread[] childThreads = new ChildThread[threadNum];
        long[] startPos = new long[threadNum];
        long endPos = 0;

        try {
            this.fileName = urlStr;
            System.out.println("fileName = " + fileName);
            System.out.println("fileDir = " + fileDir);
            this.threadLength = length / threadNum;

            // 第一步，分析已下载的临时文件，设置断点，如果是新的下载任务，则建立目标文件。
            startPos = setThreadBreakpoint(fileDir, fileName, length, startPos);
            //---------------------------------------------------线程池管理线程－－－－－－－－－－－－－－－－
            // 第二步，分多个线程下载文件
            ExecutorService exec = Executors.newCachedThreadPool();
            for (int i = 0; i < threadNum; i++) {
                // 创建子线程来负责下载数据，每段数据的起始位置为(threadLength * i + 已下载长度)
                startPos[i] += threadLength * i;
                /*
                 * 设置子线程的终止位置，非最后一个线程即为(threadLength * (i + 1) - 1)
                 * 最后一个线程的终止位置即为下载内容的长度
                 */
                if (i == threadNum - 1) {
                    endPos = length;
                } else {
                    endPos = threadLength * (i + 1) - 1;
                }
                // 开启子线程，并执行。
                ChildThread thread = new ChildThread(this, latch, i,
                        startPos[i], endPos);
                childThreads[i] = thread;
                exec.execute(thread);
            }

            try {
                // 等待CountdownLatch信号为0，表示所有子线程都结束。
                latch.await();
                exec.shutdown();

                // 第三步，把分段下载下来的临时文件中的内容写入目标文件中。
                tempFileToTargetFile(childThreads);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return fileDir + fileName;
        } finally {
        }
    }

    //将目标文件写到临时文件中
    private void tempFileToTargetFile(ChildThread[] childThreads) {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(fileDir + fileName));

            // 遍历所有子线程创建的临时文件，按顺序把下载内容写入目标文件中
            for (int i = 0; i < threadNum; i++) {
                if (statusError) {
                    for (int k = 0; k < threadNum; k++) {
                        if (childThreads[k].tempFile.length() == 0) {
                            childThreads[k].tempFile.delete();
                        }
                    }
                    System.out.println("本次下载任务不成功，请重新设置线程数。");
                    break;
                }

                BufferedInputStream inputStream = new BufferedInputStream(
                        new FileInputStream(childThreads[i].tempFile));
                System.out.println("Now is file " + childThreads[i].id);
                int len = 0;
                long count = 0;
                byte[] b = new byte[1024];
                while ((len = inputStream.read(b)) != -1) {
                    count += len;
                    outputStream.write(b, 0, len);
                    if ((count % 4096) == 0) {
                        outputStream.flush();
                    }
                }

                inputStream.close();
                // 删除临时文件
                if (childThreads[i].status == ChildThread.STATUS_HAS_FINISHED) {
                    childThreads[i].tempFile.delete();
                }
            }

            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断文件是否之前存在
    private long[] setThreadBreakpoint(String fileDir2, String fileName2,
                                       long contentLength, long[] startPos) {
        File file = new File(fileDir + fileName);
        long localFileSize = file.length();

        if (file.exists()) {
            System.out.println("file " + fileName + " has exists!");
            // 下载的目标文件已存在，判断目标文件是否完整
            if (localFileSize < contentLength) {
                System.out.println("Now download continue ... ");

                // 遍历目标文件的所有临时文件，设置断点的位置，即每个临时文件的长度
                File tempFileDir = new File(fileDir);
                File[] files = tempFileDir.listFiles();
                for (int k = 0; k < files.length; k++) {
                    String tempFileName = files[k].getName();
                    // 临时文件的命名方式为：目标文件名+"_"+编号
                    if (tempFileName != null && files[k].length() > 0
                            && tempFileName.startsWith(fileName + "_")) {
                        int fileLongNum = Integer.parseInt(tempFileName
                                .substring(tempFileName.lastIndexOf("_") + 1,
                                        tempFileName.lastIndexOf("_") + 2));
                        // 为每个线程设置已下载的位置
                        startPos[fileLongNum] = files[k].length();
                    }
                }
            }
        } else {
            // 如果下载的目标文件不存在，则创建新文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return startPos;
    }

    /**
     * @author annegu
     * @since 2009-07-16
     */
    public class ChildThread extends Thread {
        public static final int STATUS_HASNOT_FINISHED = 0;
        public static final int STATUS_HAS_FINISHED = 1;
        public static final int STATUS_HTTPSTATUS_ERROR = 2;
        private DownloadTask task;
        private int id;
        private long startPosition;
        private long endPosition;
        private final CountDownLatch latch;
        // private RandomAccessFile tempFile = null;
        private File tempFile = null;
        //线程状态码
        private int status = ChildThread.STATUS_HASNOT_FINISHED;

        public ChildThread(DownloadTask task, CountDownLatch latch, int id,
                           long startPos, long endPos) {
            super();
            this.task = task;
            this.id = id;
            this.startPosition = startPos;
            this.endPosition = endPos;
            this.latch = latch;

            try {
                tempFile = new File(this.task.fileDir + this.task.fileName
                        + "_" + id);
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            System.out.println("Thread " + id + " run ...");
            HttpURLConnection con = null;
            InputStream inputStream = null;
            BufferedOutputStream outputStream = null;
            long count = 0;
            long threadDownloadLength = endPosition - startPosition;

            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        tempFile.getPath(), true));
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            }

            for (; ; ) {
                try {
                    // 打开URLConnection
                    con = (HttpURLConnection) task.url.openConnection();
                    setHeader(con);
                    con.setAllowUserInteraction(true);
                    // 设置连接超时时间为10000ms
                    con.setConnectTimeout(10000);
                    // 设置读取数据超时时间为10000ms
                    con.setReadTimeout(10000);

                    if (startPosition < endPosition) {
                        // 设置下载数据的起止区间
                        con.setRequestProperty("Range", "bytes="
                                + startPosition + "-" + endPosition);
                        System.out.println("Thread " + id
                                + " startPosition is " + startPosition);
                        System.out.println("Thread " + id + " endPosition is "
                                + endPosition);

                        //判断http status是否为HTTP/1.1 206 Partial Content或者200 OK
                        //如果不是以上两种状态，把status改为STATUS_HTTPSTATUS_ERROR
                        if (con.getResponseCode() != HttpURLConnection.HTTP_OK
                                && con.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
                            System.out.println("Thread " + id + ": code = "
                                    + con.getResponseCode() + ", status = "
                                    + con.getResponseMessage());
                            status = ChildThread.STATUS_HTTPSTATUS_ERROR;
                            this.task.statusError = true;
                            outputStream.close();
                            con.disconnect();
                            System.out.println("Thread " + id + " finished.");
                            latch.countDown();
                            break;
                        }

                        inputStream = con.getInputStream();
                        int len = 0;
                        byte[] b = new byte[1024];
                        while (!this.task.statusError
                                && (len = inputStream.read(b)) != -1) {
                            outputStream.write(b, 0, len);

                            count += len;
                            startPosition += len;
                            // 每读满4096个byte（一个内存页），往磁盘上flush一下
                            if (count % 4096 == 0) {
                                outputStream.flush();
                            }
                        }

                        if (count >= threadDownloadLength) {
                            status = ChildThread.STATUS_HAS_FINISHED;
                        }
                        outputStream.flush();
                        outputStream.close();
                        inputStream.close();
                        con.disconnect();
                    } else {
                        status = ChildThread.STATUS_HAS_FINISHED;
                    }


                    System.out.println("Thread " + id + " finished.");
                    latch.countDown();
                    break;
                } catch (IOException e) {
                    try {
                        outputStream.flush();
                        TimeUnit.SECONDS.sleep(getSleepSeconds());
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    continue;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    con.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setHeader(URLConnection con) {
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
        con.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
        con.setRequestProperty("Accept-Encoding", "aa");
        con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        con.setRequestProperty("Keep-Alive", "300");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
        con.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
        con.setRequestProperty("Cache-Control", "max-age=0");
        con.setRequestProperty("Referer", "http://www.skycn.com/soft/14857.html");
    }

    public long getSleepSeconds() {
        return sleepSeconds;
    }

    public void setSleepSeconds(long sleepSeconds) {
        this.sleepSeconds = sleepSeconds;
    }

}