package com.feng.project.repository;

import com.feng.project.domain.JobLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog,Integer> {
	List<JobLog> findByJobId(Integer jobId);
}
