package com.xxx.exception;

/**
 * @description:重复秒杀异常,不需要我们手动去try catch
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/25 18:55
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
