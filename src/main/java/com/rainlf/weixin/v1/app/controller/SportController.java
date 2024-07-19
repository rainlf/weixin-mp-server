package com.rainlf.weixin.v1.app.controller;

import com.rainlf.weixin.v1.app.dto.ApiResp;
import com.rainlf.weixin.v1.app.dto.SportInfoDto;
import com.rainlf.weixin.v1.domain.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author rain
 * @date 6/14/2024 8:14 PM
 */
@Slf4j
@RestController
@RequestMapping("/api/sport")
public class SportController {
    @Autowired
    private GameService gameService;

    @PostMapping("/record")
    public ApiResp<Void> saveSportInfo(@RequestBody SportInfoDto sportInfoDto) {
        log.info("saveSportInfo, req: {}", sportInfoDto);
        gameService.saveSportInfo(sportInfoDto);
        return ApiResp.success();
    }
}
