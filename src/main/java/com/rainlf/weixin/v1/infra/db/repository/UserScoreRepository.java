package com.rainlf.weixin.v1.infra.db.repository;

import com.rainlf.weixin.v1.domain.consts.UserScoreTypeEnum;
import com.rainlf.weixin.v1.infra.db.entity.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Integer> {

    List<UserScore> findByUserId(Integer userId);

    List<UserScore> findByGameIdIn(List<Integer> gameIds);

    List<UserScore> findByUserIdAndType(Integer userId, UserScoreTypeEnum type);
}
