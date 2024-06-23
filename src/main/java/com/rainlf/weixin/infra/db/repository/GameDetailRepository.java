package com.rainlf.weixin.infra.db.repository;

import com.rainlf.weixin.infra.db.model.GameDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Repository
public interface GameDetailRepository extends JpaRepository<GameDetail, Integer> {
}