package com.feng.project.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


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
    	file.delete();
    	list.add(sb.toString());
    	return list;
    }
}
