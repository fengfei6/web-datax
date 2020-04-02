package com.feng.project.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feng.project.domain.JobLog;
import com.feng.project.repository.JobLogRepository;

@Service
public class JobLogService {
    @Autowired
    private JobLogRepository jobLogRepository;
    
    public void save(JobLog jobLog) {
    	jobLog.setCreateTime(new Date());
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
}
