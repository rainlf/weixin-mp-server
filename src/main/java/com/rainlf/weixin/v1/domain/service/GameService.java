package com.rainlf.weixin.v1.domain.service;

import com.rainlf.weixin.v1.app.dto.MahjongLogDto;
import com.rainlf.weixin.v1.app.dto.MahjongGameDto;
import com.rainlf.weixin.v1.app.dto.SportInfoDto;
import com.rainlf.weixin.v1.app.dto.UserMahjongTagDto;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
public interface GameService {
    List<Integer> getMahjongPlayerIds();

    void addMahjongPlayer(Integer id);

    void deleteMahjongPlayer(Integer id);

    void saveMahjongInfo(MahjongGameDto mahjongGameDto);

    void saveSportInfo(SportInfoDto sportInfoDto);

    List<UserMahjongTagDto> getUserMahjongTags(List<Integer> userIds);

    List<MahjongLogDto> getMahjongLogs();
}
