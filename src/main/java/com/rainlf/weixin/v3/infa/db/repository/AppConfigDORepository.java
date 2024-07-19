package com.rainlf.weixin.v3.infa.db.repository;

import com.rainlf.weixin.v3.infa.db.entity.AppConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rain
 * @date 7/19/2024 2:29 PM
 */
@Repository
public interface AppConfigDORepository extends JpaRepository<AppConfigDO, Integer> {

    AppConfigDO findByKey(String key);
}
