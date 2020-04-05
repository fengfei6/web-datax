package com.feng.project.repository;

import com.feng.project.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Integer> {
    Job findJobByName(String name);

    @Query(value = "select * from job where name like %?1% ", nativeQuery = true)
    List<Job> findJobsByName(String name);
}
