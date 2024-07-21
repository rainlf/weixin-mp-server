package com.rainlf.weixin.v3.domain.mahjong;

import com.rainlf.weixin.v3.app.dto.MjLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 6:57 AM
 */
@Slf4j
@Service
public class MjServiceImpl implements MjService {
    @Override
    public List<MjLogDTO> getMjLogs() {
        return List.of();
    }
}
