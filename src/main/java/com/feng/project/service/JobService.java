package com.feng.project.service;

import java.util.Date;
import java.util.List;

import com.feng.project.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feng.project.domain.Job;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    
    public void save(Job job) {
    	job.setCreateTime(new Date());
    	job.setUpdateTime(new Date());
    	jobRepository.save(job);
    }
    
    public void delete(Integer id) {
    	jobRepository.deleteById(id);
    }
    
    public void update(Job job) {
    	job.setUpdateTime(new Date());
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
}
