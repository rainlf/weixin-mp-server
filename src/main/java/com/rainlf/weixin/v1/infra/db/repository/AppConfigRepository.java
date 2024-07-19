package com.rainlf.weixin.v1.infra.db.repository;

import com.rainlf.weixin.v1.infra.db.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rain
 * @date 6/23/2024 8:19 PM
 */
@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {
}
