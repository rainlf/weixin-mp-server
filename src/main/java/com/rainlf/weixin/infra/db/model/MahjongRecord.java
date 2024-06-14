package com.rainlf.weixin.infra.db.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@TableName(value = "weixin_mahjong_record")
public class MahjongRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String roundId;
    private Integer recorderId;
    private String type;
    private Integer userId;
    private Integer score;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}