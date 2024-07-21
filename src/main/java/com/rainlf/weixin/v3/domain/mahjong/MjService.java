package com.rainlf.weixin.v3.domain.mahjong;

import com.rainlf.weixin.v3.app.dto.DeskGameDTO;
import com.rainlf.weixin.v3.app.dto.OnlineGameDTO;
import com.rainlf.weixin.v3.domain.mahjong.model.MjGameLog;
import com.rainlf.weixin.v3.domain.mahjong.model.MjPlayer;

import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 6:18 AM
 */
public interface MjService {
    List<MjGameLog> getMjLogs();

    List<MjGameLog> getUserMjLogs(Integer userId);

    List<MjPlayer> getMjPlayers();

    List<MjPlayer> getLatestMjPlayers();

    void saveOnlieGame(OnlineGameDTO onlineGameDTO);

    void saveDeskGame(DeskGameDTO deskGameDTO);
}
