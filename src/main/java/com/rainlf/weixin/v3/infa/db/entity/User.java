package com.rainlf.weixin.v3.infa.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 7/19/2024 2:09 PM
 */
@Data
@Slf4j
@Entity
@Table(name = "weixin_user")
@SQLRestriction("is_deleted = 0")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String openId;
    private String nickname;
    private byte[] avatar;
    private Integer coin;
    private String comment;
    @Column(name = "is_admin")
    private boolean admin;
    @Column(name = "is_deleted", insertable = false)
    private boolean deleted;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;

    public void addScore(Integer score) {
        Assert.notNull(score, "score must not be null");
        log.info("{} add score: {}", nickname, score);
        this.coin = this.coin + score;
    }
}
