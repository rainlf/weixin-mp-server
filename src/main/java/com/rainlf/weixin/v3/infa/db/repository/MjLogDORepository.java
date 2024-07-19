package com.rainlf.weixin.v3.infa.db.repository;

import com.rainlf.weixin.v3.infa.db.entity.MjLogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rain
 * @date 7/19/2024 2:30 PM
 */
@Repository
public interface MjLogDORepository extends JpaRepository<MjLogDO, Integer> {
}
