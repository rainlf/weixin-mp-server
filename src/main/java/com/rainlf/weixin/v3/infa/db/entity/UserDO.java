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
@Table(name = "weixin_user")
@SQLRestriction("is_deleted = 0")
public class UserDO {
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
}
