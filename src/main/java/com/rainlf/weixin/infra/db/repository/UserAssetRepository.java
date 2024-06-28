package com.rainlf.weixin.infra.db.repository;

import com.rainlf.weixin.infra.db.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Repository
public interface UserAssetRepository extends JpaRepository<UserAsset, Integer> {

    Optional<UserAsset> findByUserId(Integer userId);

    List<UserAsset> findByUserIdIn(Iterable<Integer> userIds);
}
