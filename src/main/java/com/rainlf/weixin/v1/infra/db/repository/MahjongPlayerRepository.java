package com.rainlf.weixin.v1.infra.db.repository;

import com.rainlf.weixin.v1.infra.db.entity.MahjongPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rain
 * @date 6/22/2024 9:18 PM
 */
@Repository
public interface MahjongPlayerRepository extends JpaRepository<MahjongPlayer, Integer> {

    List<MahjongPlayer> findByUserId(Integer userId);
}
