package com.rainlf.weixin.v3.infa.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 7/19/2024 2:09 PM
 */
@Data
@Entity
@Table(name = "weixin_app_config")
@SQLRestriction("is_deleted = 0")
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String key;
    private String value;
    @Column(name = "is_deleted", insertable = false)
    private boolean deleted;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
