package com.happyheng.controller;

import com.happyheng.entity.response.FileUploadResponse;
import com.happyheng.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 *
 */
@RestController
public class FileController {

    @Value("${file.path}")
    private String filePath;

    @Value("${file.download.path}")
    private String fileDownloadPath;


    @RequestMapping("test")
    public FileUploadResponse test(){
        FileUploadResponse uploadResponse = new FileUploadResponse();
        uploadResponse.setFilePath("test");
        return uploadResponse;
    }

    @ResponseBody
    @RequestMapping("/up")
    public FileUploadResponse upload(HttpServletRequest request) throws IllegalStateException, IOException, NoSuchAlgorithmException {

        String fileHttpPath = "";
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //记录上传过程起始时的时间，用来计算上传时间
                int pre = (int) System.currentTimeMillis();
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为空,说明该文件存在，否则说明该文件不存在
                    if (!myFileName.trim().isEmpty()) {
                        System.out.println(myFileName);

                        String fileName = getRondomFileName() + getFileType(myFileName);
                        //定义本地路径
                        String path = filePath + fileName;
                        File localFile = new File(path);
                        file.transferTo(localFile);

                        fileHttpPath = fileDownloadPath + fileName;
                    }
                }
                //记录上传该文件后的时间
                int finaltime = (int) System.currentTimeMillis();
                System.out.println(finaltime - pre);
            }

        }
        FileUploadResponse uploadResponse = new FileUploadResponse();
        uploadResponse.setFilePath(fileHttpPath);
        return uploadResponse;
    }

    @RequestMapping("/download/{fileName:.+}")
    public void download(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        //获取下载文件露肩
        String downLoadPath = filePath + fileName;
        //获取文件的长度
        long fileLength = new File(downLoadPath).length();
        //设置文件输出类型
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
        //设置输出长度
        response.setHeader("Content-Length", String.valueOf(fileLength));
        //获取输入流
        bis = new BufferedInputStream(new FileInputStream(downLoadPath));
        //输出流
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        //关闭流
        bis.close();
        bos.close();
    }


    private String getFileType(String fileName) {
        String[] splitText = fileName.split("[.]");
        if (splitText.length == 0) {
            return "";
        }
        return "." + splitText[splitText.length - 1];
    }

    private String getRondomFileName() throws NoSuchAlgorithmException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        StringBuilder builder = new StringBuilder(dateFormat.format(new Date()));
        builder.append((int) (Math.random() * 1000));

        return MD5Utils.getMD5(builder.toString());
    }

}
