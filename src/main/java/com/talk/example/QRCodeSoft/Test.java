package com.talk.example.QRCodeSoft;

/**
 * Created by zhongjing on 2016/06/23 0023.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        //生成不带logo的二维码
        String text1 = "182********";
        QRCodeUtil.encode(text1, "", "d:/cscs", true);

        //生成带logo的二维码

        String text2 = "http://www.dans88.com.cn";
        QRCodeUtil.encode(text2, "d:/MyWorkDoc/my180.jpg", "d:/MyWorkDoc", true);

    }
}
