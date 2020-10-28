package com.xxx.service;

import com.xxx.dao.SeckillMapper;
import com.xxx.dao.SuccessKilledMapper;
import com.xxx.dto.Exposer;
import com.xxx.dto.SeckillExecution;
import com.xxx.entity.Seckill;
import com.xxx.entity.SuccessKilled;
import com.xxx.enums.SeckillStatEnum;
import com.xxx.exception.RepeatKillException;
import com.xxx.exception.SeckillCloseException;
import com.xxx.exception.SeckillException;
import com.xxx.service.interfaces.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/25 19:12
 */

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /*加入一个盐值，用于混淆*/
    private final String salt = "thisIsASaltValue";

    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private SuccessKilledMapper successKilledMapper;

    /**
     *
     * @description: 查询全部秒杀商品记录
     * @param
     * @return:      数据库中所有的秒杀商品记录
     * @time:        2020/10/26 0:38
     */
    @Override
    public List<Seckill> getSeckillList() {
        return seckillMapper.queryAll(0, 4);
    }

    /**
     *
     * @description: 查询单个秒杀商品记录
     * @param        seckillId 商品ID
     * @return:      根据ID查询出来的记录信息
     * @time:        2020/10/26 0:39
     */
    @Override
    public Seckill getById(long seckillId) {
        return seckillMapper.queryById(seckillId);
    }

    /**
     *
     * @description: 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀地址
     * @param        seckillId 秒杀商品ID
     * @return:      根据对应的状态返回对应的状态实体
     * @time:        2020/10/26 0:40
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillMapper.queryById(seckillId);
        System.out.println(seckill);
        if(seckill == null){
            logger.warn("查询不到这个秒杀商品的记录");
            return new Exposer(false,null,  seckillId, null, null, null);
        }else{
//            return null;
        }
        LocalDateTime startTime = seckill.getStartTime();
        LocalDateTime endTime = seckill.getEndTime();
        LocalDateTime nowTime = LocalDateTime.now();
        if(nowTime.isAfter(startTime) && nowTime.isBefore(endTime)){
            String md5 = getMd5(seckillId);
            return new Exposer(true, md5, seckillId, null, null, null);
        }
        return new Exposer(false, null, seckillId, nowTime, startTime, endTime);
    }

    private String getMd5(long seckillId){
        String base = seckillId + "/" +salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     *
     * @description: 执行秒杀操作，失败就抛出异常
     * @param seckillId 秒杀的商品ID
     * @param userPhone 用户手机
     * @param md5 加密值
     * @return:      根据不同的结果返回不同的实体信息
     * @time:        2020/10/26 17:03
     */
    @Transactional
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            logger.error("秒杀数据被篡改");
            throw new SeckillException("seckill data rewrited");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        try {
            // 记录购买行为
            int insertCount = successKilledMapper.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存 ，热点商品的竞争
                int reduceNumber = seckillMapper.reduceNumber(seckillId, nowTime);
                if (reduceNumber <= 0) {
                    logger.warn("没有更新数据库记录，说明秒杀结束");
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功了，返回那条插入成功秒杀的信息  进行commit
                    SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e1) {
            throw e1;
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            return new SeckillExecution(seckillId, SeckillStatEnum.DATE_REWRITE);
        }
        LocalDateTime killTime = LocalDateTime.now();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        try{
            seckillMapper.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if(result == 1){
                SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            }else{
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);

        }
    }
}
