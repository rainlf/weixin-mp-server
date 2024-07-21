package com.rainlf.weixin.v3.app.controller;

import com.rainlf.weixin.v3.app.dto.MjLogDTO;
import com.rainlf.weixin.v3.app.dto.MjPlayerDTO;
import com.rainlf.weixin.v3.app.dto.MjRankDTO;
import com.rainlf.weixin.v3.app.dto.base.ApiResp;
import com.rainlf.weixin.v3.domain.mahjong.MjService;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;
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

    @GetMapping("/logs")
    public ApiResp<List<MjLogDTO>> getMjLogs() {
        return ApiResp.success(mjService.getMjLogs());
    }

    @GetMapping("/ranks")
    public ApiResp<MjRankDTO> getMjRanks() {
        return ApiResp.success(mjService.getMjRanks());
    }

    @GetMapping("/user/{userId}/logs")
    public ApiResp<List<MjLogDTO>> getUserMjLogs(@PathVariable("userId") Integer userId) {
        return ApiResp.success(mjService.getUserMjLogs(userId));
    }

    @GetMapping("/players")
    public ApiResp<List<MjPlayerDTO>> getMjPlayers() {
        return ApiResp.success(mjService.getMjPlayers());
    }

    @GetMapping("/players/latest")
    public ApiResp<List<MjPlayerDTO>> getLatestMjPlayers() {
        return ApiResp.success(mjService.getLatestMjPlayers());
    }

    @GetMapping("/point/operators")
    public ApiResp<List<MjPointOperatorEnum>> getMjPointOperators() {
        return ApiResp.success(List.of(MjPointOperatorEnum.values()));
    }
}
