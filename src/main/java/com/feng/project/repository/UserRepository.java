package com.feng.project.repository;

import com.feng.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author 小可爱
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "update user set password=?2 where email=?1")
    @Transactional
    @Modifying
    void updateUserPassByEmail(String email, String pass);

    Optional<User> findUserByNameAndPassword(String name, String password);

    @Transactional
    @Modifying
    @Query(value = "update user  SET role =?2  where id=?1", nativeQuery = true)
    Integer modifyRole(Integer id,String role);

    
    @Transactional
    @Modifying
    @Query(value = "update user  SET password =?2  where id=?1", nativeQuery = true)
    Integer modifyPass(Integer id,String password);
}
