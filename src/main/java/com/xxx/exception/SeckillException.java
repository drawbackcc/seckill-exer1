package com.xxx.exception;

/**
 * @description:秒杀基础异常
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/25 18:56
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message){
        super(message);
    }

    public SeckillException(String message, Throwable cause){
        super(message, cause);
    }
}
