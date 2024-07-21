package com.rainlf.weixin.v3.app.controller.advice;

import com.rainlf.weixin.v3.app.dto.ApiResp;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author rain
 * @date 5/30/2024 3:20 PM
 */
@Slf4j
@ControllerAdvice
public class AdviceController {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResp<String> handleException(Exception exception, HttpServletResponse response) {
        log.error("biz api error", exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 这将设置HTTP状态码为400
        return ApiResp.failure(exception.getMessage());
    }
}

