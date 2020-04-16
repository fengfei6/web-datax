package com.feng.project.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.feng.project.repository.JobRepository;
import com.feng.project.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feng.project.domain.Job;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    
    public void save(Job job) {
        Map<String,String> map = JsonUtil.testComplexJSONStrToJSONObject(job.getJsonContent());
        job.setReaderDbType(map.get("reader").replace("reader", ""));
        job.setWriterDbType(map.get("writer").replace("writer", ""));
    	job.setCreateTime(new Date(System.currentTimeMillis()));
    	jobRepository.save(job);
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
