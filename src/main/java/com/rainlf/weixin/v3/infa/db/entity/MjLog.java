package com.rainlf.weixin.v3.infa.db.entity;

import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjUserTypeEnum;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjWinTypeEnum;
import com.rainlf.weixin.v3.infa.db.converter.MjPointOperatorEnumsConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rain
 * @date 7/19/2024 2:12 PM
 */
@Data
@Entity
@Table(name = "weixin_mj_log")
@SQLRestriction("is_deleted = 0")
public class MjLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String gameId;
    private Integer userId;
    private MjUserTypeEnum userType;
    private MjWinTypeEnum winType;
    private Integer point;
    @ElementCollection
    @Convert(converter = MjPointOperatorEnumsConverter.class)
    private List<MjPointOperatorEnum> pointOperators;
    private Integer totalPoint;
    private Integer score;
    @Column(name = "is_deleted", insertable = false)
    private boolean deleted;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
