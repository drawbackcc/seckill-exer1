package com.xxx.dto;

import lombok.Data;

/**
 * @description:
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/25 18:55
 */
@Data
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;
    public SeckillResult() {
    }

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    @Override
    public String toString() {
        return "SeckillResult{" +
                "状态=" + success +
                ", 数据=" + data +
                ", 错误消息='" + error + '\'' +
                '}';
    }
}
