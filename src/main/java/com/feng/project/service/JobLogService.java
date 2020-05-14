package com.feng.project.service;

import java.util.List;

import com.feng.project.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feng.project.domain.JobLog;
import com.feng.project.repository.JobLogRepository;

@Service
public class JobLogService {
    @Autowired
    private JobLogRepository jobLogRepository;
    
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

    public JobLog findLogByJobId(Integer jobId){ return jobLogRepository.findByJobId(jobId);}

}
