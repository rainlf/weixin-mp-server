package com.rainlf.weixinmpserver.service;

import com.rainlf.weixinmpserver.model.Game;

import java.util.List;

/**
 * @author rain
 * @date 5/30/2024 4:16 PM
 */
public interface GameService {

    void record(List<Game> games);
}
