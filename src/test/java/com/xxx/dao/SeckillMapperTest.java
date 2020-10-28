package com.xxx.dao;

import com.xxx.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext-dao.xml"})
public class SeckillMapperTest {
    @Resource
    private SeckillMapper seckillMapper;

    @Test
    public void reduceNumber() {
        long seckillId = 1000;
        LocalDateTime localDateTime = LocalDateTime.now();
        int i = seckillMapper.reduceNumber(seckillId, localDateTime);
        System.out.println("reduce seckill:" + i);
    }

    @Test
    public void queryById() {
        long seckillId = 1000;
        Seckill seckill = seckillMapper.queryById(seckillId);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillMapper.queryAll(0, 10);
        for(Seckill seckill : seckills){
            System.out.println(seckill);
        }
    }

    @Test
    public void killByProcedure() {
        Map<String, Object> params = new HashMap<>();
        params.put("seckillId", 1003);
        params.put("phone", "15813321988");
        params.put("killTime", LocalDateTime.now());
        params.put("result", -100);
        seckillMapper.killByProcedure(params);
        System.out.println(params.get("result"));
    }
}
