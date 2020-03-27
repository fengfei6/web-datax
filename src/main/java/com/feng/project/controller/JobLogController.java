package com.feng.project.controller;

import com.feng.project.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class JobLogController {
    @Autowired
    private JobLogService jobLogService;
}
