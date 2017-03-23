package com.test;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by zhongjing on 2016/1/28 0028.
 */
public class Test {
    public static void main(String[] args) throws Exception{
        //将application/x-www-form-urlencoded字符串转换成普通字符串
        String keyword = URLDecoder.decode("%B7%E8%BF%F1%B5%C4java%BD%B2%D2%E5","GBK");
        System.out.println(keyword);

        //将普通字符串转换成application/x-www-form-urlencoded字符串
        String urlStr = URLEncoder.encode("疯狂的java讲义","GBK");
        System.out.println(urlStr);
    }
}
