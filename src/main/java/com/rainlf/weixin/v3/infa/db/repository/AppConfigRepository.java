package com.rainlf.weixin.v3.infa.db.repository;

import com.rainlf.weixin.v3.infa.db.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author rain
 * @date 7/19/2024 2:29 PM
 */
@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {

    Optional<AppConfig> findByKey(String key);
}
