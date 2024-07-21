package com.rainlf.weixin.v3.infa.db.repository;

import com.rainlf.weixin.v3.infa.db.entity.MjLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rain
 * @date 7/19/2024 2:30 PM
 */
@Repository
public interface MjLogRepository extends JpaRepository<MjLog, Integer> {

    // find latest 10 logs which match userId and (gametype is not null or pointOperator is not null)
    @Query(value = "select * from weixin_mj_log where user_id = :userId and (game_type is not null or point_operators is not null) and is_deleted = 0 order by create_time desc limit 10", nativeQuery = true)
    List<MjLog> findLatest10LogWithTags(@Param("userId") Integer userId);

    List<MjLog> findByUserId(Integer userId);

    @Query(value = "select max(create_time) from weixin_mj_log where user_id = :userId and is_deleted = 0", nativeQuery = true)
    LocalDateTime findUserLastGameTime(Integer userId);

    // find latest logs with the same game id
    @Query(value = "select * from weixin_mj_log where game_id = (select game_id from weixin_mj_log where is_deleted = 0 order by create_time desc limit 1)", nativeQuery = true)
    List<MjLog> findLatestMjGameLog();
}
