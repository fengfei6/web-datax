package com.feng.project.repository;

import com.feng.project.domain.JobLog;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog,Integer> {
	JobLog findByJobId(Integer jobId);

	Optional<JobLog> findJobLogByJobId(Integer jobId);
}
