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
    
    public Datasource findDatasourceByName(String name) {
    	return datasourceRepository.findDatasourceByName(name);
    }

    public Datasource findDatasourceByDbnameAndIpAndPort(String dbname,String ip,String port){
        return datasourceRepository.findByDbnameAndIpAndPort(dbname,ip,port);
    }

    public List<Datasource> findDatasourcesByTypeAndName(String type,String name){
        return datasourceRepository.findDatasourcesByNameAndType(type,name);
    }

    public List<Datasource> findDatasourcesByName(String name){
        return datasourceRepository.findDatasourcesByName(name);
    }
}
