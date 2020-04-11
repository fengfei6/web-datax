package com.feng.project.service;

import com.feng.project.domain.CronJob;
import com.feng.project.repository.CronJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CronJobService {
    @Autowired
    private CronJobRepository cronJobRepository;

    public CronJob save(CronJob cronJob){
        cronJob.setCreateTime(new Date());
        cronJob.setUpdateTime(new Date());
        return cronJobRepository.save(cronJob);
    }

    public CronJob update(CronJob cronJob){
        cronJob.setUpdateTime(new Date());
        return cronJobRepository.save(cronJob);
    }

    public void delete(CronJob cronJob){
        cronJobRepository.delete(cronJob);
    }

    public CronJob getOne(Integer id){
        return cronJobRepository.getOne(id);
    }

    public List<CronJob> finAll(){
        return cronJobRepository.findAll();
    }

    public CronJob findJobByName(String name){ return cronJobRepository.findCronJobByName(name);}

    public List<CronJob> findJobsByName(String name){return cronJobRepository.findCronJobsByName(name);}

    public List<CronJob> findJobsByNameAndIsRunning(String name,Integer isRunning){return cronJobRepository.findCronJobsByNameAndIsRunning(name,isRunning);}

    public List<CronJob> findJobsByUserId(Integer userId){return cronJobRepository.findCronJobsByUserId(userId);}

    public List<CronJob> findJobsByNameAndUserId(String name,Integer userId){return cronJobRepository.findCronJobsByNameAndUserId(name,userId);}

    public List<CronJob> findJobsByNameAndIsRunningAndUserId(String name,Integer isRunning,Integer userId){return cronJobRepository.findCronJobsByNameAndIsRunningAndUserId(name,isRunning,userId);}

    public CronJob getJobByNameAndUserId(String name,Integer userId){ return cronJobRepository.getCronJobByNameAndUserId(name,userId);}
}
