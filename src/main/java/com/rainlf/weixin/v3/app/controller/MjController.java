package com.rainlf.weixin.v3.app.controller;

import com.alibaba.fastjson2.JSON;
import com.rainlf.weixin.v3.app.dto.*;
import com.rainlf.weixin.v3.app.dto.ApiResp;
import com.rainlf.weixin.v3.app.mapper.MjDTOMapper;
import com.rainlf.weixin.v3.domain.mahjong.MjService;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;
import com.rainlf.weixin.v3.domain.mahjong.model.MjGameLog;
import com.rainlf.weixin.v3.domain.mahjong.model.MjPlayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/onlie/game")
    public ApiResp<Void> saveOnlieGame(@RequestBody OnlineGameDTO onlineGameDTO) {
        log.info("saveOnlieGame onlineGameDTO:{}", JSON.toJSONString(onlineGameDTO));
        Assert.isTrue(onlineGameDTO.isValid(), "onlineGameDTO is invalid");
        mjService.saveOnlieGame(onlineGameDTO);
        return ApiResp.success();
    }

    @PostMapping("/desk/game")
    public ApiResp<Void> saveDeskGame(@RequestBody DeskGameDTO deskGameDTO) {
        log.info("saveDeskGame deskGameDTO:{}", JSON.toJSONString(deskGameDTO));
        Assert.isTrue(deskGameDTO.isValid(), "deskGameDTO is invalid");
        mjService.saveDeskGame(deskGameDTO);
        return ApiResp.success();
    }
}
