package com.feng.project.repository;

import com.feng.project.domain.CronJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobRepository extends JpaRepository<CronJob,Integer> {
    CronJob findCronJobByName(String name);
}
