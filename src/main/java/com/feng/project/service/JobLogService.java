package com.feng.project.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.feng.project.domain.Job;
import com.feng.project.model.IJobHandler;
import com.feng.project.repository.JobRepository;
import com.feng.project.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feng.project.domain.JobLog;
import com.feng.project.repository.JobLogRepository;

@Service
public class JobLogService {
    @Autowired
    private JobLogRepository jobLogRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private XxlJobService xxlJobService;
    
    public void save(JobLog jobLog) {
    	jobLogRepository.save(jobLog);
    }
    
    public void delete(Integer id) {
    	jobLogRepository.deleteById(id);
    }
    
    public void update(JobLog jobLog) {
    	jobLogRepository.save(jobLog);
    }
    
    public JobLog getOne(Integer id) {
    	return jobLogRepository.getOne(id);
    }
    
    public List<JobLog> findAll(){
    	return jobLogRepository.findAll();
    }
    
    public List<JobLog> findByJobId(Integer jobId){
    	return jobLogRepository.findByJobId(jobId);
    }


    public void saveIntoLog(JSONObject content) throws UnsupportedEncodingException {
        //joblog id
        String id = content.get("id").toString();
        List<JobLog> instances = jobLogRepository.findAllByExecid(id);
        JobLog jobLog = new JobLog();

        String triggerTime = getFormatTime(content.get("triggerTime"));
        String handleTime = getFormatTime(content.get("handleTime"));
        int triggerCode = Integer.valueOf(content.get("triggerCode").toString());
        int handleCode = Integer.valueOf(content.get("handleCode").toString());

        if(instances.size() == 1){
            jobLog = instances.get(0);
            Boolean isSame = isSame(jobLog,handleTime,handleCode,triggerTime,triggerCode);
            if(isSame){
                return;
            }
        }else if(instances.size() > 1){
            jobLogRepository.deleteByExecid(id);
        }

        int taskId = Integer.valueOf(content.get("jobId").toString());

        //获取job信息
        Job job = jobRepository.findJobByTaskId(taskId);
        if(job != null){

            //instance.setId(id);
            jobLog.setTaskId(taskId);
            jobLog.setJobId(job.getId());
            jobLog.setName(job.getName());
            jobLog.setExecutorParam(content.get("executorParam")==null?null:content.get("executorParam").toString());
            jobLog.setExecutorAddress(content.get("executorAddress").toString());
            jobLog.setExecid(content.get("id").toString());
            jobLog.setTriggerTime(triggerTime);
            jobLog.setTriggerCode(triggerCode);
            jobLog.setUserId(job.getUserId());
            String msg = content.get("triggerMsg").toString();
            jobLog.setTriggerMsg(new String(msg.getBytes("iso8859-1"),"utf-8"));


            jobLog.setStatus(handleStatus(triggerCode,handleCode));
            jobLog.setHandleCode(handleCode);
            jobLog.setHandleTime(handleTime);
            jobLog.setHandleMsg(content.get("handleMsg")==null?"":content.get("handleMsg").toString());


            jobLogRepository.save(jobLog);
        }
    }

    private String handleStatus(int triggerCode, int handleCode) {
        if(triggerCode == IJobHandler.FAIL.getCode()){
            return "FAILED";
        }else{
            if(handleCode == IJobHandler.SUCCESS.getCode()){
                return "SUCCEEDED";
            }else if(handleCode == IJobHandler.FAIL.getCode()){
                return "FAILED";
            }else if(handleCode == IJobHandler.FAIL_TIMEOUT.getCode()){
                return "FAILED";
            }else{
                return "RUNNING";
            }
        }
    }

    private String getFormatTime(Object obj) {
        return obj==null?"":obj.toString().replace("T"," ").replace(".000+0000","");
    }

    private Boolean isSame(JobLog jobLog, String handleTime, int handleCode, String triggerTime, int triggerCode) {
        return (jobLog.getHandleTime().equals(handleTime)
                && handleCode == jobLog.getHandleCode()
                && (handleCode == IJobHandler.SUCCESS.getCode() || handleCode == IJobHandler.FAIL.getCode()))
                || (jobLog.getTriggerTime().equals(triggerTime)
                && triggerCode == IJobHandler.FAIL.getCode()
                && triggerCode == jobLog.getTriggerCode());
    }

    public String getExecLog(Integer id, int offset) throws UnsupportedEncodingException {
        JobLog jobLog = jobLogRepository.getOne(id);
        Long triggetTime = DateUtil.dealDateFormatToLong(jobLog.getTriggerTime());
        JSONObject jsonObject = xxlJobService.getExeclog(
                String.valueOf(triggetTime),jobLog.getExecid(),offset);
        String content = jsonObject.get("logContent").toString();
        return new String(content.getBytes("iso8859-1"),"utf-8");
    }

}
