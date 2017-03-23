package com.talk.example.down;

/**
 * Created by zhongjing on 2016/1/28 0028.
 */
public class MultiThreadDown {
    public static void main(String[] args) throws Exception{
        //初始化DownUtil对象
        final DownUtil downUtil = new DownUtil("http://www.crazyit.org/attachment.php?aid=MTY0NXxjNjBjBi" +
                "YzNj3wxMzE1NTQ2MjU5fGNhODlKVmpXVmhpNGlkWmVzR2JZbnluZWpqSllOd3JzckdodXJOMUpOWWt0aTJz",
                "oracelsql.rar",4);
        //开始下载
        downUtil.download();
        new Thread(){
            public void run(){
                while (downUtil.getCompleteRate()<1){
                    //每隔0.1秒查询一次任务的完成进度
                    //GUI程序中可根据该进度来绘制进度条
                    System.out.println("已完成"+downUtil.getCompleteRate());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
