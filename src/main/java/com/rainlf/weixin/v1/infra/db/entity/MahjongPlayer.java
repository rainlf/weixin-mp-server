package com.rainlf.weixin.v1.infra.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 6/22/2024 9:16 PM
 */
@Data
@Entity
@Table(name = "weixin_mahjong_player")
@SQLRestriction("is_deleted = 0")
public class MahjongPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    @Column(insertable = false)
    private boolean isDeleted;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
