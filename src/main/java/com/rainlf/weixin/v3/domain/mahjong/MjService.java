package com.rainlf.weixin.v3.domain.mahjong;

import com.rainlf.weixin.v3.app.dto.MjLogDTO;
import com.rainlf.weixin.v3.app.dto.MjPlayerDTO;
import com.rainlf.weixin.v3.app.dto.MjRankDTO;

import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 6:18 AM
 */
public interface MjService {
    List<MjLogDTO> getMjLogs();

    List<MjLogDTO> getUserMjLogs(Integer userId);

    MjRankDTO getMjRanks();

    List<MjPlayerDTO> getMjPlayers();

    List<MjPlayerDTO> getLatestMjPlayers();
}
