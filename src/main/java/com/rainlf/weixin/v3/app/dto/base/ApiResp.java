package com.rainlf.weixin.v3.app.dto.base;

import lombok.Data;

/**
 * @author rain
 * @date 6/14/2024 4:12 PM
 */
@Data
public class ApiResp<T> {
    private boolean success;
    private T data;
    private String errorMsg;

    private ApiResp(boolean success, T data, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public static <T> ApiResp<T> success() {
        return new ApiResp<>(true, null, null);
    }

    public static <T> ApiResp<T> success(T data) {
        return new ApiResp<>(true, data, null);
    }

    public static <T> ApiResp<T> failure(String errorMsg) {
        return new ApiResp<>(false, null, errorMsg);
    }
}
