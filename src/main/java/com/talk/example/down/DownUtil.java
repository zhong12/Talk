package com.talk.example.down;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhongjing on 2016/1/28 0028.
 */
public class DownUtil {
    /**
     * 定义下载资源路径
     */
    private String path;
    /**
     * 指定所下载的文件的保存位置
     */
    private String targetFile;
    /**
     * 定义需要使用多少个线程下载资源
     */
    private int threadNum;
    /**
     * 定义下载的线程对象
     */
    private DownThread[] threads;
    /**
     * 定义下载的文件的总大小
     */
    private int fileSize;

    public DownUtil(String path, String targetFile, int threadNum) {
        this.path = path;
        this.targetFile = targetFile;
        this.threadNum = threadNum;
        threads = new DownThread[threadNum];
    }


    public void download() throws Exception {
        URL url = new URL(this.path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5 * 1000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "image/gif,image/jpeg,image/pjpeg,application/x-shockwave-flash," +
                "application/xaml+xml,application/vnd.ms-xpsdocument,application/x-ms-xbap," +
                "application/x-ms-application,application/vnd.ms-excel," +
                "application/vnd.ms-powerpoint,application/msword,*/*");
        connection.setRequestProperty("Accept-Language", "zh-CN");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");

        //得到文件大小
        fileSize = connection.getContentLength();
        connection.disconnect();
        int currentPartSize = fileSize / threadNum + 1;
        RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
        //设置本地文件的大小
        file.setLength(fileSize);
        file.close();
        for (int i = 0; i < threadNum; i++) {
            //计算每个线程下载的开始位置
            int startPos = i * currentPartSize;
            //每个线程使用一个RandomAccessFile进行下载
            RandomAccessFile currentPart = new RandomAccessFile(targetFile, "rw");
            //定位该线程的下载位置
            currentPart.seek(startPos);
            //创建下载线程
            DownThread downThread = new DownThread(startPos, currentPartSize, currentPart);
            threads[i] = downThread;
            //启动下载线程
            threads[i].start();
        }
    }


    public double getCompleteRate() {
        //统计多个线程已经下载的总大小
        int sumSize = 0;
        for (int i = 0; i < threadNum; i++) {
            sumSize += threads[i].length;
        }
        return sumSize * 1.0 / fileSize;
    }


    private class DownThread extends Thread {
        //当前线程的下载位置
        private int startPos;
        //定义当前线程负责下载的文件大小
        private int currentPartSize;
        //当前线程需要下载的文件块
        private RandomAccessFile currentPart;
        //定义该线程已下载的字节数
        public int length;

        public DownThread(int startPos, int currentPartSize, RandomAccessFile currentPart) {
            this.startPos = startPos;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
        }

        public void run() {
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5 * 1000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "image/gif,image/jpeg,image/pjpeg,application/x-shockwave-flash," +
                        "application/xaml+xml,application/vnd.ms-xpsdocument,application/x-ms-xbap," +
                        "application/x-ms-application,application/vnd.ms-excel," +
                        "application/vnd.ms-powerpoint,application/msword,*/*");
                connection.setRequestProperty("Accept-Language", "zh-CN");
                connection.setRequestProperty("Charset", "UTF-8");

                InputStream inputStream = connection.getInputStream();
                //跳过startPos 个字节，表明该线程只下载自己负责的那部分文件
                inputStream.skip(this.startPos);
                byte[] buffer = new byte[1024];
                int hasRead = 0;
                //读取网络数据，并写入本地文件
                while (length < currentPartSize && (hasRead = inputStream.read(buffer)) != -1) {
                    currentPart.write(buffer, 0, hasRead);
                    //累计该线程下载的总大小
                    length += hasRead;
                }
                currentPart.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
