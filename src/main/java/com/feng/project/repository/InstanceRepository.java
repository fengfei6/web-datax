package com.feng.project.repository;

import com.feng.project.domain.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface InstanceRepository extends JpaRepository<Instance,Integer> {
    List<Instance> findByExecid(String execid);

    void deleteByExecid(String execid);

    @Query(value = "select * from instance where user_id =?1 order by handle_time desc", nativeQuery = true)
    List<Instance> findAllByUserId(Integer userId);

    @Query(value = "select * from instance order by handle_time desc", nativeQuery = true)
    List<Instance> findInstances();

    List<Instance> findAllByCronjobId(Integer cronjobId);

    List<Instance> findAllByUserIdAndCronjobId(Integer userId,Integer cronjobId);

    @Transactional
    void deleteInstancesByCronjobId(Integer cronjobId);
}
