package com.rainlf.weixin.infra.db.repository;

import com.rainlf.weixin.infra.db.entity.MahjongGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Repository
public interface MahjongGameRepository extends JpaRepository<MahjongGame, Integer> {

    List<MahjongGame> findByIdIn(List<Integer> ids);

}
