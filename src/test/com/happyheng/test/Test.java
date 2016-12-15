package com.happyheng.test;

/**
 *
 * Created by happyheng on 16/12/10.
 */
public class Test {


    @org.junit.Test
    public void test(){
        String type = getFileType("sss.sss.txt");
        System.out.println("结果为" + type);
    }

    @org.junit.Test
    public void testSplit() {
        String text = "xx|xx|xx";
        String[] splitTextList = text.split("[|]");
        for (String splitText : splitTextList) {
            System.out.println("结果为" + splitText);
        }
    }

    private String getFileType(String fileName) {
        String[] splitText = fileName.split("[.]");
        if (splitText.length == 0){
            return "";
        }
        return "." + splitText[splitText.length-1];
    }

}
