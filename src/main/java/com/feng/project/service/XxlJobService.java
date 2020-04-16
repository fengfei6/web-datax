package com.feng.project.service;

import com.alibaba.fastjson.JSONObject;
import com.feng.project.domain.CronJob;
import com.feng.project.domain.Job;
import com.feng.project.util.XxlUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
public class XxlJobService {
    
    public String submitCronJob(CronJob cronJob) {
        MultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();

        linkedMultiValueMap.add("JobGroup", "1");
        if(cronJob.getCronExpress() != null){
            //定时任务
            linkedMultiValueMap.add("jobCron", cronJob.getCronExpress());
        }
        linkedMultiValueMap.add("jobDesc", cronJob.getName());
        linkedMultiValueMap.add("author", "admin");
        //默认
        linkedMultiValueMap.add("alarmEmail", "fengfei4168@163.com");
        //单机串行
        linkedMultiValueMap.add("executorBlockStrategy", "SERIAL_EXECUTION");
        //一致性HASH
        linkedMultiValueMap.add("executorRouteStrategy", "CONSISTENT_HASH");

        linkedMultiValueMap.add("glueType", "GLUE_SHELL");

        linkedMultiValueMap.add("executorParam", "");

        linkedMultiValueMap.add("glueSource",setGlueSource(cronJob.getName(),cronJob.getUserId()));
        if(cronJob.getTaskId() == null){
            return CronJobinfoWithAdd(cronJob,linkedMultiValueMap);
        }else{
            return CronJobinfoWithUpdate(cronJob,linkedMultiValueMap);
        }
    }

    private String CronJobinfoWithUpdate(CronJob cronJob, MultiValueMap<String, String> linkedMultiValueMap) {
        linkedMultiValueMap.add("id", String.valueOf(cronJob.getTaskId()));
        JSONObject jsonObject = XxlUtil.handleSubmitJobinfoWithUpdate(linkedMultiValueMap);
        if(!jsonObject.get("code").toString().equals("200")){
            System.out.println("submitCronJobFail");
        }
        return null;
    }

    private String CronJobinfoWithAdd(CronJob cronJob, MultiValueMap<String, String> linkedMultiValueMap) {
        JSONObject jsonObject = XxlUtil.handleSubmitJobinfoWithAdd(linkedMultiValueMap);
        //save glueSource
        if(jsonObject.get("code").toString().equals("200") ) {
            String taskId = jsonObject.get("content").toString();
            return taskId;
        }else{
            System.out.println("submitCronJobFail");
        }
        return "0";
    }

    public String submitJob(Job job) {
        MultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();

        linkedMultiValueMap.add("JobGroup", "1");
        linkedMultiValueMap.add("jobCron", "0 0 */1 * * ?");
        linkedMultiValueMap.add("jobDesc", job.getName());
        linkedMultiValueMap.add("author", "admin");
        //默认
        linkedMultiValueMap.add("alarmEmail", "fengfei4168@163.com");
        //单机串行
        linkedMultiValueMap.add("executorBlockStrategy", "SERIAL_EXECUTION");
        //一致性HASH
        linkedMultiValueMap.add("executorRouteStrategy", "CONSISTENT_HASH");

        linkedMultiValueMap.add("glueType", "GLUE_SHELL");

        linkedMultiValueMap.add("executorParam", "");

        linkedMultiValueMap.add("glueSource",setGlueSource(job.getName(),job.getUserId()));
        if(job.getTaskId() == null){
            return JobinfoWithAdd(job,linkedMultiValueMap);
        }else{
            return JobinfoWithUpdate(job,linkedMultiValueMap);
        }
    }

    private String JobinfoWithUpdate(Job job, MultiValueMap<String, String> linkedMultiValueMap) {
        linkedMultiValueMap.add("id", String.valueOf(job.getTaskId()));
        JSONObject jsonObject = XxlUtil.handleSubmitJobinfoWithUpdate(linkedMultiValueMap);
        if(!jsonObject.get("code").toString().equals("200")){
            System.out.println("submitJobFail");
        }
        return null;
    }

    private String JobinfoWithAdd(Job job, MultiValueMap<String, String> linkedMultiValueMap) {
        JSONObject jsonObject = XxlUtil.handleSubmitJobinfoWithAdd(linkedMultiValueMap);
        //save glueSource
        if(jsonObject.get("code").toString().equals("200") ) {
            String taskId = jsonObject.get("content").toString();
            return taskId;
        }else{
            System.out.println("submitJobFail");
        }
        return "0";
    }


    private String setGlueSource(String cronJobName,Integer userId){
        String source = "#!/bin/bash\n" +
                "echo \"xxl-job: hello shell\"\n" +
                "\n" +
                "echo \"脚本位置：$0\"\n" +
                "echo \"任务参数：$1\"\n" +
                "echo \"分片序号 = $2\"\n" +
                "echo \"分片总数 = $3\"\n" +
                "\n" +
                "python /root/datax/bin/datax.py /root/datax/job/"+cronJobName+"_"+userId+".json\n" +
                "\n" +
                "echo \"Good bye!\"\n" +
                "exit 0\n";
        return source;
    }

    public String onScheduling(CronJob cronJob) {
        Map<String, String> map = Maps.newHashMap();
        if (cronJob.getCronExpress() != null) {
            //定时任务
            map.put("id", String.valueOf(cronJob.getTaskId()));
            JSONObject jsonObject = XxlUtil.onSchedulingWithCron(map);
            if (!jsonObject.get("code").toString().equals("200")) {
                System.out.println("submitCronJobFail");
            }
        }
        return "1";
    }

        public String cancelScheduling(CronJob cronJob) {
            Map<String, String> map = Maps.newHashMap();
            if (cronJob.getCronExpress() != null) {
                //定时任务
                map.put("id", String.valueOf(cronJob.getTaskId()));
                JSONObject jsonObject = XxlUtil.cancelSchedulingWithCron(map);
                if (!jsonObject.get("code").toString().equals("200")) {
                    System.out.println("submitCronJobFail");
                }
            }
            return "1";
        }

        public JSONObject getHandleInfo(Integer taskId){
                Map<String, String> map = Maps.newHashMap();
                map.put("jobId", String.valueOf(taskId));
                JSONObject jsonObject = XxlUtil.getHandleInfo(map);
                if (!jsonObject.get("code").toString().equals("200")) {
                    System.out.println("fail");
                }
                return jsonObject;
        }

    public List<JSONObject> getAllHandleInfo() {
        JSONObject jsonObject = XxlUtil.getAllHandleInfo();
        if(!"200".equals(jsonObject.get("code").toString())){
            System.out.println("fail");
        }
        return (List<JSONObject>)jsonObject.get("content");
    }

    public JSONObject getExeclog(String triggerTime, String execid,int offset){
                Map<String, String> map = Maps.newHashMap();
                map.put("executorAddress", "172.21.0.8:9999");
                map.put("triggerTime", triggerTime);
                map.put("logId", execid);
                map.put("fromLineNum", String.valueOf(offset));
                JSONObject jsonObject = XxlUtil.getExeclog(map);
                if (!jsonObject.get("code").toString().equals("200")) {
                    System.out.println("fail");
                }
                return (JSONObject) jsonObject.get("content");
         }

         public JSONObject delete (Integer taskId){
                Map<String, String> map = Maps.newHashMap();
                map.put("id", String.valueOf(taskId));
                JSONObject jsonObject = XxlUtil.delete(map);
                if (!jsonObject.get("code").toString().equals("200")) {
                    System.out.println("fail");
                }
                return jsonObject;
        }

    public JSONObject executeUnder(Integer taskId, String executorParam) {
        Map<String, String> map = Maps.newHashMap();
        map.put("id",String.valueOf(taskId));
        map.put("executorParam",executorParam);
        JSONObject jsonObject = XxlUtil.executeUnder(map);
        if(!jsonObject.get("code").toString().equals("200")){
            System.out.println("fail");
        }
        return jsonObject;
    }

}
