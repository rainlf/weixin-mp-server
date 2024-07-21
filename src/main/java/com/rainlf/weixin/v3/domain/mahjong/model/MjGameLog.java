package com.rainlf.weixin.v3.domain.mahjong.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author rain
 * @date 7/21/2024 3:19 PM
 */
@Data
public class MjGameLog {
    private String gameId;
    private List<MjUserLog> mjLogs;

    public MjGameLog() {
        this.gameId = "mj-" + UUID.randomUUID().toString().replace("-", "");
        this.mjLogs = new ArrayList<>();
    }

    public MjGameLog(String gameId, List<MjUserLog> mjLogs) {
        this.gameId = gameId;
        this.mjLogs = mjLogs;
    }
}
