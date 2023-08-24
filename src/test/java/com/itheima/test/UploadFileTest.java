package com.itheima.test;

import org.junit.jupiter.api.Test;

public class UploadFileTest {
    @Test
    public void test1(){
        String fileName = "jjhlsf.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));//截取后缀
        System.out.println(suffix);

    }
}
