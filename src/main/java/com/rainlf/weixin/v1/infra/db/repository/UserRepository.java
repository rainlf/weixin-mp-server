package com.rainlf.weixin.v1.infra.db.repository;

import com.rainlf.weixin.v1.infra.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByOpenId(String openId);

    List<User> findAll();
}