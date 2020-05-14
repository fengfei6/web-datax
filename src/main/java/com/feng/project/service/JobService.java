package com.feng.project.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ch.ethz.ssh2.Connection;
import com.feng.project.domain.JobLog;
import com.feng.project.repository.JobLogRepository;
import com.feng.project.repository.JobRepository;
import com.feng.project.util.DataxUtil;
import com.feng.project.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.feng.project.domain.Job;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobLogRepository jobLogRepository;

    @Value("${datax.ip}")
    private String ip;

    @Value("${datax.username}")
    private String username;

    @Value("${datax.password}")
    private String password;

    @Value("${datax.path}")
    private String path;

    @Value("${datax.job.path}")
    private String job_path;
    
    public void save(Job job) {
        Map<String,String> map = JsonUtil.testComplexJSONStrToJSONObject(job.getJsonContent());
        job.setReaderDbType(map.get("reader").replace("reader", ""));
        job.setWriterDbType(map.get("writer").replace("writer", ""));
    	job.setCreateTime(new Date(System.currentTimeMillis()));
    	jobRepository.save(job);
    }

    @Async
    public void doJobAndSaveLog(Integer id) throws Exception{
        Job job = jobRepository.getOne(id);
        Connection conn = DataxUtil.login(ip, username, password);
        JobLog jobLog = null;
        Optional<JobLog> optionalJobLog = jobLogRepository.findJobLogByJobId(id);
        if(!optionalJobLog.isPresent()) {
            jobLog = new JobLog(job.getName(), job.getId(), new Date(System.currentTimeMillis()), "任务正在执行，请稍后再试", "RUNNING");
            jobLog = jobLogRepository.save(jobLog);
        }else {
            jobLog = optionalJobLog.get();
            jobLog.setStatus("RUNNING");
            jobLog.setLogResult("任务正在执行，请稍后再试");
            jobLog = jobLogRepository.save(jobLog);
        }
        String result = DataxUtil.execmd(conn,"python "+path+" "+job_path+job.getName() + "_" + job.getUserId() + ".json");
        jobLog.setLogResult(result);
        jobLog.setStatus("SUCCESS");
        jobLogRepository.save(jobLog);
    }

    public void delete(Integer id) {
    	jobRepository.deleteById(id);
    }
    
    public void update(Job job) {
    	jobRepository.save(job);
    }
    
    public Job getOne(Integer id) {
    	return jobRepository.getOne(id);
    }
    
    public List<Job> findAll(){
    	return jobRepository.findAll();
    }

    public Job findJobByName(String name){ return jobRepository.findJobByName(name);}

    public List<Job> findJobsByName(String name){ return jobRepository.findJobsByName(name);}

    public List<Job> findJobsByNameAndUserId(String name,Integer userId){return jobRepository.findJobsByNameAndUserId(name,userId);}

    public List<Job> findJobsByUserId(Integer userId){return jobRepository.findJobsByUserId(userId);}

    public Job getJobByNameAndUserId(String name,Integer userId){ return jobRepository.getJobByNameAndUserId(name,userId);}
}
