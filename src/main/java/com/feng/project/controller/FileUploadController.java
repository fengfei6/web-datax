package com.feng.project.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.feng.project.util.DataxUtil;

import ch.ethz.ssh2.Connection;

@Controller
public class FileUploadController {
    // 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
    public final static String UPLOAD_PATH_PREFIX = "static/file/";
    
    @ResponseBody
    @PostMapping("/upload")
    public List<String> upload(MultipartFile files) throws IOException {
    	List<String> list = new ArrayList<>();
    	if(files.isEmpty()){
            return list;
        }
        String realPath = new String("src/main/resources/" + UPLOAD_PATH_PREFIX);
        File file = new File(realPath);
        String name = files.getOriginalFilename();
        //构建真实的文件路径
        File newFile = new File(file.getAbsolutePath() + File.separator + name);
        //转存文件到指定路径，如果文件名重复的话，将会覆盖掉之前的文件,这里是把文件上传到 “绝对路径”
        files.transferTo(newFile);
        
        FileReader fr = new FileReader("src/main/resources/static/file/"+name); 
    	StringBuilder sb = new StringBuilder();
    	int ch = 0;  
    	while((ch = fr.read())!=-1 ){   
    		sb.append((char)ch);   
    	} 
    	list.add(sb.toString());
    	return list;
    }

    @PostMapping("/exec")
    public String dataxService(String filepath) throws IOException {
        String ip = "192.144.129.188";
        String username = "root";
        String password = "FFei916#";
        String cmd = "python /root/dataX/datax/bin/datax.py /root/dataX/datax/job/test.json";
        Connection connection = DataxUtil.login(ip, username, password);
        DataxUtil.transferFile(connection,filepath,"/root/dataX/datax/job");
        String execmd = DataxUtil.execmd(connection, cmd,"test");
        return "admin/result";
    }

    @GetMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName = "job.log";
        if (fileName != null){
            String realPath = "src/main/resources/static/log/log.log";
            File file = new File(realPath);
            fileName = new String(file.getName().getBytes("utf-8"));
            if (file.exists()){
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition","attachment;fileName="+fileName);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try{
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer,0,i);
                        i = bis.read(buffer);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (bis != null){
                        try{
                            bis.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (fis != null){
                        try{
                            fis.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return fileName;

    }
}
