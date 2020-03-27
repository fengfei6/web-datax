package com.feng.project.service;

import com.feng.project.domain.Datasource;
import com.feng.project.repository.DatasourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class DatasourceService {
    @Autowired
    private DatasourceRepository datasourceRepository;

    public void save(Datasource datasource){
        datasource.setCreateTime(new Date());
        datasource.setUpdateTime(new Date());
        datasourceRepository.save(datasource);
    }

    public List<Datasource> findAll(){
        return datasourceRepository.findAll();
    }

    public Datasource getDatasource(Integer id){
        return datasourceRepository.getOne(id);
    }
    
    public void update(Datasource datasource) {
    	datasource.setUpdateTime(new Date());
    	datasourceRepository.save(datasource);
    }
    
    public void delete(Integer id) {
    	datasourceRepository.deleteById(id);
    }
}
