package com.rainlf.weixin.v1.domain.consts;

import lombok.Getter;

/**
 * @author rain
 * @date 6/17/2024 2:35 PM
 */
@Getter
public enum MahjongWinCaseEnum {
    MJ_COMMON_WIN(1, 1, "胡牌"),
    MJ_SELF_TOUCH_WIN(1, 3, "自摸"),
    MJ_ONE_PAO_DOUBLE_WIN(2, 1, "一炮双响"),
    MJ_ONE_PAO_TRIPLE_WIN(3, 1, "一炮三响"),
    ;

    private final int winnerNumber;
    private final int loserNumber;
    private final String name;

    MahjongWinCaseEnum(int winnerNumber, int loserNumber, String name) {
        this.winnerNumber = winnerNumber;
        this.loserNumber = loserNumber;
        this.name = name;
    }
}
