package com.feng.project.service;

import com.alibaba.fastjson.JSONObject;
import com.feng.project.domain.CronJob;
import com.feng.project.domain.Instance;
import com.feng.project.model.IJobHandler;
import com.feng.project.repository.CronJobRepository;
import com.feng.project.repository.InstanceRepository;
import com.feng.project.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class InstanceService {
    @Autowired
    private InstanceRepository instanceRepository;
    @Autowired
    private CronJobRepository cronJobRepository;
    @Autowired
    private XxlJobService xxlJobService;

    public void saveIntoInstance(JSONObject content) throws UnsupportedEncodingException {
        //joblog id
        String id = content.get("id").toString();
        List<Instance> instances = instanceRepository.findByExecid(id);
        Instance instance = new Instance();

        String triggerTime = getFormatTime(content.get("triggerTime"));
        String handleTime = getFormatTime(content.get("handleTime"));
        int triggerCode = Integer.valueOf(content.get("triggerCode").toString());
        int handleCode = Integer.valueOf(content.get("handleCode").toString());

        if(instances.size() == 1){
            instance = instances.get(0);
            Boolean isSame = isSame(instance,handleTime,handleCode,triggerTime,triggerCode);
            if(isSame){
                return;
            }
        }else if(instances.size() > 1){
            instanceRepository.deleteByExecid(id);
        }

        int taskId = Integer.valueOf(content.get("jobId").toString());

        //获取job信息
        CronJob job = cronJobRepository.getCronJobByTaskId(taskId);
        if(job != null){

            //instance.setId(id);
            instance.setTaskId(taskId);
            instance.setCronjobId(job.getId());
            instance.setName(job.getName());
            instance.setCronExpression(job.getCronExpress());
            instance.setExecutorParam(content.get("executorParam")==null?null:content.get("executorParam").toString());
            instance.setExecutorAddress(content.get("executorAddress").toString());
            instance.setExecid(content.get("id").toString());
            instance.setTriggerTime(triggerTime);
            instance.setTriggerCode(triggerCode);
            instance.setUserId(job.getUserId());
            String msg = content.get("triggerMsg").toString();
            instance.setTriggerMsg(new String(msg.getBytes("iso8859-1"),"utf-8"));


            instance.setStatus(handleStatus(triggerCode,handleCode));
            instance.setHandleCode(handleCode);
            instance.setHandleTime(handleTime);
            instance.setHandleMsg(content.get("handleMsg")==null?"":content.get("handleMsg").toString());


            instanceRepository.save(instance);
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

    private Boolean isSame(Instance instance, String handleTime, int handleCode, String triggerTime, int triggerCode) {
        return (instance.getHandleTime().equals(handleTime)
                && handleCode == instance.getHandleCode()
                && (handleCode == IJobHandler.SUCCESS.getCode() || handleCode == IJobHandler.FAIL.getCode()))
                || (instance.getTriggerTime().equals(triggerTime)
                && triggerCode == IJobHandler.FAIL.getCode()
                && triggerCode == instance.getTriggerCode());
    }

    public String getExecLog(Integer id, int offset) throws UnsupportedEncodingException {
        Instance instance = instanceRepository.getOne(id);
        Long triggetTime = DateUtil.dealDateFormatToLong(instance.getTriggerTime());
        JSONObject jsonObject = xxlJobService.getExeclog(
                String.valueOf(triggetTime),instance.getExecid(),offset);
        String content = jsonObject.get("logContent").toString();
        return new String(content.getBytes("iso8859-1"),"utf-8");
    }

    public List<Instance> findAll(){
        return instanceRepository.findInstances();
    }

    public List<Instance> findAllByUserId(Integer userId){
        return instanceRepository.findAllByUserId(userId);
    }

    public List<Instance> findAllByUserIdAndCronjobId(Integer userId,Integer cronjobId){ return instanceRepository.findAllByUserIdAndCronjobId(userId,cronjobId);}

    public Instance getOne(Integer id){
        return instanceRepository.getOne(id);
    }

    public List<Instance> findAllByCronjobId(Integer cronjobId){return instanceRepository.findAllByCronjobId(cronjobId);}
}
