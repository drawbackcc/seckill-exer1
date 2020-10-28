package com.xxx.exception;

/**
 * @description:秒杀已经关闭异常,当秒杀结束就会出现这个异常
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/25 19:02
 */
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }


}
