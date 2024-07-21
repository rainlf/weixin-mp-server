package com.rainlf.weixin.v3.app.controller;

import com.rainlf.weixin.v3.app.dto.MjLogDTO;
import com.rainlf.weixin.v3.app.dto.MjRankDTO;
import com.rainlf.weixin.v3.app.dto.base.ApiResp;
import com.rainlf.weixin.v3.domain.mahjong.MjService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
