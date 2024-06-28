package com.rainlf.weixin.infra.db.entity;

import com.rainlf.weixin.domain.consts.MahjongFanEnum;
import com.rainlf.weixin.domain.consts.MahjongWinCaseEnum;
import com.rainlf.weixin.infra.db.converter.ListMahjongFanEnumConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@Entity
@Table(name = "weixin_mahjong_game")
public class MahjongGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer refereeUserId;
    private Integer baseScore;
    @Convert(converter = ListMahjongFanEnumConverter.class)
    private List<MahjongFanEnum> fanList;
    private MahjongWinCaseEnum winCase;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}