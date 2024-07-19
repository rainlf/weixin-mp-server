package com.rainlf.weixin.v1.app.dto;

import com.rainlf.weixin.v1.domain.consts.MahjongFanEnum;
import com.rainlf.weixin.v1.domain.consts.MahjongWinCaseEnum;
import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 6/17/2024 11:20 AM
 */
@Data
public class MahjongGameDto {
    private Integer recorderId;
    private List<Integer> winnerIds;
    private List<Integer> loserIds;
    private MahjongWinCaseEnum winCase;
    private Integer baseFan;
    private List<MahjongFanEnum> fanList;
}
