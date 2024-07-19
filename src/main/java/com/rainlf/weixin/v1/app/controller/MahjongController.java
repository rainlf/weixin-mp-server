package com.rainlf.weixin.v1.app.controller;

import com.rainlf.weixin.v1.app.dto.ApiResp;
import com.rainlf.weixin.v1.app.dto.MahjongLogDto;
import com.rainlf.weixin.v1.app.dto.MahjongGameDto;
import com.rainlf.weixin.v1.app.dto.UserMahjongTagDto;
import com.rainlf.weixin.v1.domain.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:14 PM
 */
@Slf4j
@RestController
@RequestMapping("/api/mahjong")
public class MahjongController {
    @Autowired
    private GameService gameService;

    @GetMapping("/palyer/ids")
    public ApiResp<List<Integer>> getPlayerIds() {
        log.info("getPlayerIds");
        List<Integer> playerIds = gameService.getMahjongPlayerIds();
        log.info("getPlayerIds, playerIds: {}", playerIds);
        return ApiResp.success(playerIds);
    }

    @PostMapping("/palyer")
    public ApiResp<Void> addPalyer(@RequestParam("id") Integer id) {
        log.info("addPalyer, id: {}", id);
        gameService.addMahjongPlayer(id);
        return ApiResp.success();
    }

    @DeleteMapping("/palyer")
    public ApiResp<Void> deletePlayer(@RequestParam("id") Integer id) {
        log.info("deletePlayer, id: {}", id);
        gameService.deleteMahjongPlayer(id);
        return ApiResp.success();
    }


    @PostMapping("/game")
    public ApiResp<Void> saveMahjongGame(@RequestBody MahjongGameDto mahjongGameDto) {
        log.info("saveMahjongInfo, req: {}", mahjongGameDto);
        gameService.saveMahjongInfo(mahjongGameDto);
        return ApiResp.success();
    }

    @PostMapping("/getUserTags")
    public ApiResp<List<UserMahjongTagDto>> getUserMahjongTags(@RequestBody List<Integer> userIds) {
        log.info("getUserMahjongTags, userIds: {}", userIds);
        return ApiResp.success(gameService.getUserMahjongTags(userIds));
    }

    @GetMapping("/logs")
    public ApiResp<List<MahjongLogDto>> getMahjongLogs() {
        log.info("getMahjongLogs");
        List<MahjongLogDto> result = gameService.getMahjongLogs();
        log.info("getMahjongLogs, size: {}", result.size());
        return ApiResp.success(result);
    }
}
