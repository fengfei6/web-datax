package com.feng.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Controller
public class HelloController{

    @PostMapping("/uploadFile")
    public String upload(MultipartFile[] fileUpload,Model model)  {
        model.addAttribute("uploadStatus","success");
        for(MultipartFile file :fileUpload ){
            String fileName = file.getOriginalFilename();
            fileName = UUID.randomUUID()+"-"+fileName;
            String dirPath = "file/";
            File filePath = new File(dirPath);
            System.out.println(filePath);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            try {
                file.transferTo(new File(dirPath + fileName));
            }catch(Exception e){
                e.printStackTrace();
                model.addAttribute("uploadStatus","上传失败");
            }
        }
        return "upload";
    }
}
