package com.xxx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description:暴露秒杀地址接口
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/25 18:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exposer {
    /*是否开启秒杀 */
    private boolean exposed;
    /*  对秒杀地址进行加密措施  */
    private String md5;
    /* id为seckillId的商品秒杀地址   */
    private long seckillId;
    /* 系统当前的时间   */
    private LocalDateTime now;
    /* 秒杀开启的时间   */
    private LocalDateTime start;
    /*  秒杀结束的时间  */
    private LocalDateTime end;

//    public Exposer(boolean exposed, long seckillId){
//        this.exposed = exposed;
//        this.seckillId = seckillId;
//    }
//
//    public Exposer(boolean exposed, String md5, long seckillId){
//        this.exposed = exposed;
//        this.md5 = md5;
//        this.seckillId = seckillId;
//    }

    @Override
    public String toString() {
        return "Exposer{" +
                "秒杀状态=" + exposed +
                ", md5加密值='" + md5 + '\'' +
                ", 秒杀ID=" + seckillId +
                ", 当前时间=" + now +
                ", 开始时间=" + start +
                ", 结束=" + end +
                '}';
    }

}
