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

    public List<Datasource> findDatasourcesByTypeAndNameAndUserId(String type,String name,Integer userId){
        return datasourceRepository.findDatasourcesByNameAndTypeAndUserId(type,name,userId);
    }

    public List<Datasource> findDatasourcesByNameAndUserId(String name,Integer userId){
        return datasourceRepository.findDatasourcesByNameAndUserId(name,userId);
    }


    public List<Datasource> findDatasourcesByUserId(Integer userId){
        return datasourceRepository.findDatasourcesByUserId(userId);
    }
    
    public Datasource getDatasourceByDbnameAndUserId(String dbname,Integer userId){
        return datasourceRepository.getDatasourceByDbnameAndUserId(dbname,userId);
    }

    public List<Datasource> getDatasourcesByTypeAndUserId(String type,Integer userId){
        return datasourceRepository.getDatasourcesByTypeAndUserId(type,userId);
    }

    public List<Datasource> getDatasourcesByType(String type){
        return datasourceRepository.getDatasourcesByType(type);
    }
}
