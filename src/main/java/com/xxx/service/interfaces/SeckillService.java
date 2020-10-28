package com.xxx.service.interfaces;

import com.xxx.dto.Exposer;
import com.xxx.dto.SeckillExecution;
import com.xxx.entity.Seckill;
import com.xxx.exception.RepeatKillException;
import com.xxx.exception.SeckillCloseException;
import com.xxx.exception.SeckillException;

import java.util.List;

/**
 * @description:
 * @author:czm, PC of Chenzhimei
 * @time: 2020/10/25 18:16
 */

public interface SeckillService {

    /**
     *
     * @description:查询全部秒杀记录
     * @return:
     * @time: 2020/10/25 18:26
     */
    List<Seckill> getSeckillList();

    /**
     *
     * @description: 查询单个秒杀记录
     * @param seckillId 秒杀记录id
     * @return:      根据ID查询出来的记录信息
     * @time:        2020/10/25 18:30
     */
    Seckill getById(long seckillId);

    /**
     *
     * @description: 在秒杀开启时输出秒杀接口的地址，否则输出系统时间跟秒杀地址
     * @param
     * @return:      根据对应的状态返回对应的状态实体
     * @time:        2020/10/25 18:36
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     *
     * @description: 执行秒杀操作,有可能是失败的,失败我们就抛出异常
     * @param seckillId  秒杀的商品ID
     * @param userPhone 手机号码
     * @param md5   md5加密值
     * @return   根据不同的结果返回不同的实体信息
     * @time:        2020/10/25 19:03
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;

    SeckillExecution executeSeckillProcedure(long seckillId,long userPhone,String md5);
}
