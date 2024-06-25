package com.rainlf.weixin.app.dto;

import com.rainlf.weixin.domain.consts.MahjongFanEnum;
import com.rainlf.weixin.domain.consts.MahjongWinCaseEnum;
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
