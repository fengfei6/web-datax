package com.feng.project.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class JobScheduledService {
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private XxlJobService xxlJobService;
    /**
     * 每一小时执行一次
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void realTimeUpdateInstance() throws Exception {
        //获取远程的实例
        List<JSONObject> jobs = xxlJobService.getAllHandleInfo();
        saveIntoInstance(jobs);
    }

    private void saveIntoInstance(List<JSONObject> jobs) throws Exception {
        for(JSONObject jsonObject : jobs){
            instanceService.saveIntoInstance(jsonObject);
        }
    }

}
