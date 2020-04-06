package com.feng.project.repository;

import com.feng.project.domain.CronJob;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CronJobRepository extends JpaRepository<CronJob,Integer> {
    CronJob findCronJobByName(String name);

    @Query(value = "select * from cron_job where name like %?1% ", nativeQuery = true)
    List<CronJob> findCronJobsByName(String name);

    @Query(value = "select * from cron_job where name like %?1% and is_running = ?2", nativeQuery = true)
    List<CronJob> findCronJobsByNameAndIsRunning(String name,Integer isRunning);

    @Query(value = "select * from cron_job where name like %?1% and user_id = ?2", nativeQuery = true)
    List<CronJob> findCronJobsByNameAndUserId(String name,Integer userId);

    @Query(value = "select * from cron_job where name like %?1% and is_running = ?2 and user_id = ?3", nativeQuery = true)
    List<CronJob> findCronJobsByNameAndIsRunningAndUserId(String name,Integer isRunning,Integer userId);

    List<CronJob> findCronJobsByUserId(Integer userId);
}
