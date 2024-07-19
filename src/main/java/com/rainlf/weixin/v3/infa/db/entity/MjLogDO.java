package com.rainlf.weixin.v3.infa.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 7/19/2024 2:12 PM
 */
@Data
@Entity
@Table(name = "weixin_mj_log")
@SQLRestriction("is_deleted = 0")
public class MjLogDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer userType;
    private Integer winType;
    private Integer point;
    private String pointOperators;
    private Integer totalPoint;
    private Integer score;
    @Column(name = "is_deleted", insertable = false)
    private boolean deleted;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
