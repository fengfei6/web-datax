package com.feng.project.service;

import com.feng.project.domain.CronJob;
import com.feng.project.domain.Datasource;
import com.feng.project.repository.CronJobRepository;
import com.feng.project.repository.DatasourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class CronJobService {
    @Autowired
    private CronJobRepository cronJobRepository;
    @Autowired
    private DatasourceRepository datasourceRepository;

    public CronJob save(CronJob cronJob){
        Datasource datasources = datasourceRepository.getOne(cronJob.getReaderDbId());
        Datasource datasourcet = datasourceRepository.getOne(cronJob.getWriterDbId());
        cronJob.setIsRunning(0);
        cronJob.setReaderDbType(datasources.getType());
        cronJob.setWriterDbType(datasourcet.getType());
        if(cronJob.getReaderColumn() != null){
            cronJob.setWriterColumn(cronJob.getReaderColumn());
        }
        cronJob.setCreateTime(new Date(System.currentTimeMillis()));
        cronJob.setUpdateTime(new Date(System.currentTimeMillis()));
        return cronJobRepository.save(cronJob);
    }

    public CronJob update(CronJob cronJob){
        cronJob.setUpdateTime(new Date(System.currentTimeMillis()));
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

    public List<CronJob> findJobsByName(String name){return cronJobRepository.findCronJobsByName(name);}

    public List<CronJob> findJobsByNameAndIsRunning(String name,Integer isRunning){return cronJobRepository.findCronJobsByNameAndIsRunning(name,isRunning);}

    public List<CronJob> findJobsByUserId(Integer userId){return cronJobRepository.findCronJobsByUserId(userId);}

    public List<CronJob> findJobsByNameAndUserId(String name,Integer userId){return cronJobRepository.findCronJobsByNameAndUserId(name,userId);}

    public List<CronJob> findJobsByNameAndIsRunningAndUserId(String name,Integer isRunning,Integer userId){return cronJobRepository.findCronJobsByNameAndIsRunningAndUserId(name,isRunning,userId);}

    public CronJob getJobByNameAndUserId(String name,Integer userId){ return cronJobRepository.getCronJobByNameAndUserId(name,userId);}
}
