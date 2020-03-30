package com.feng.project.controller;

import ch.ethz.ssh2.Connection;
import com.feng.project.util.DataxUtil;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class FileUploadController {
    // 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
    public final static String UPLOAD_PATH_PREFIX = "static/file/";
    @PostMapping("/upload")
    public String upload(MultipartFile uploadFile, HttpServletRequest request, Model model) throws IOException {
        if(uploadFile.isEmpty()){
            return "请选择上传文件";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String realPath = new String("src/main/resources/" + UPLOAD_PATH_PREFIX);
        String format = sdf.format(new Date());
        File file = new File(realPath + format);
        if(!file.isDirectory()){
            //递归生成文件夹
            file.mkdirs();
        }
        String name = uploadFile.getOriginalFilename();
        //构建真实的文件路径
        File newFile = new File(file.getAbsolutePath() + File.separator + name);
        //转存文件到指定路径，如果文件名重复的话，将会覆盖掉之前的文件,这里是把文件上传到 “绝对路径”
        uploadFile.transferTo(newFile);
        model.addAttribute("file",newFile.getPath());
        return "admin/exec";
    }


    @PostMapping("/exec")
    public String dataxService(String filepath) throws IOException {
        String ip = "192.144.129.188";
        String username = "root";
        String password = "FFei916#";
        String cmd = "python /root/dataX/datax/bin/datax.py /root/dataX/datax/job/test.json";
        Connection connection = DataxUtil.login(ip, username, password);
        DataxUtil.transferFile(connection,filepath,"/root/dataX/datax/job");
        String execmd = DataxUtil.execmd(connection, cmd);
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
