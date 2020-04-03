package com.feng.project.repository;

import com.feng.project.domain.Datasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasourceRepository extends JpaRepository<Datasource,Integer> {
	Datasource findDatasourceByName(String name);

	Datasource findByDbnameAndIpAndPort(String dbname,String ip,String port);
}
