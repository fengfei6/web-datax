package com.feng.project.repository;

import com.feng.project.domain.Datasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasourceRepository extends JpaRepository<Datasource,Integer> {
	Datasource findDatasourceByName(String name);

	Datasource findByDbnameAndIpAndPort(String dbname,String ip,String port);

	@Query(value = "select * from datasource where name like %?2% and type=?1", nativeQuery = true)
	List<Datasource> findDatasourcesByNameAndType(String type,String name);

	@Query(value = "select * from datasource where name like %?1% ", nativeQuery = true)
	List<Datasource> findDatasourcesByName(String name);
}
