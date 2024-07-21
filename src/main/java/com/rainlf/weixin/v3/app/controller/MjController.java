package com.rainlf.weixin.v3.app.controller;

import com.rainlf.weixin.v3.app.dto.MjGameLogDTO;
import com.rainlf.weixin.v3.app.dto.MjPlayerDTO;
import com.rainlf.weixin.v3.app.dto.MjRankDTO;
import com.rainlf.weixin.v3.app.dto.base.ApiResp;
import com.rainlf.weixin.v3.app.mapper.MjDTOMapper;
import com.rainlf.weixin.v3.domain.mahjong.MjService;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;
import com.rainlf.weixin.v3.domain.mahjong.model.MjGameLog;
import com.rainlf.weixin.v3.domain.mahjong.model.MjPlayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 9:11 AM
 */
@Slf4j
@RestController
@RequestMapping("/mj")
public class MjController {
    @Autowired
    private MjService mjService;
    @Autowired
    private MjDTOMapper mjDTOMapper;

    @GetMapping("/logs")
    public ApiResp<List<MjGameLogDTO>> getMjLogs() {
        List<MjGameLog> mjGameLogs = mjService.getMjLogs();
        return ApiResp.success(mjDTOMapper.fromMjGameLogs(mjGameLogs));
    }

    @GetMapping("/user/{userId}/logs")
    public ApiResp<List<MjGameLogDTO>> getUserMjLogs(@PathVariable("userId") Integer userId) {
        List<MjGameLog> mjGameLogs = mjService.getUserMjLogs(userId);
        return ApiResp.success(mjDTOMapper.fromMjGameLogs(mjGameLogs));
    }

    @GetMapping("/ranks")
    public ApiResp<MjRankDTO> getMjRanks() {
        List<MjPlayer> mjPlayers = mjService.getMjPlayers();
        return ApiResp.success(mjDTOMapper.getMjRankDTOFromMjPlayers(mjPlayers));
    }

    @GetMapping("/players")
    public ApiResp<List<MjPlayerDTO>> getMjPlayers() {
        List<MjPlayer> mjPlayers = mjService.getMjPlayers();
        return ApiResp.success(mjDTOMapper.fromMjPlayers(mjPlayers));
    }

    @GetMapping("/players/latest")
    public ApiResp<List<MjPlayerDTO>> getLatestMjPlayers() {
        List<MjPlayer> mjPlayers = mjService.getLatestMjPlayers();
        return ApiResp.success(mjDTOMapper.fromMjPlayers(mjPlayers));
    }

    @GetMapping("/point/operators")
    public ApiResp<List<MjPointOperatorEnum>> getMjPointOperators() {
        return ApiResp.success(List.of(MjPointOperatorEnum.values()));
    }
}
