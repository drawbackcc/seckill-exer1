package com.xxx.service;

import com.xxx.dto.Exposer;
import com.xxx.dto.SeckillExecution;
import com.xxx.entity.Seckill;
import com.xxx.exception.RepeatKillException;
import com.xxx.exception.SeckillCloseException;
import com.xxx.service.interfaces.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext-dao.xml",
        "classpath:spring/applicationContext-service.xml"})
//在redis配置文件里配置了seckillservice的拦截切面，加redis配置文件的话会织入seckillservice代理类
//所以执行getlist这个方法时返回的是缓存的数据，而不是mysql数据库
public class SeckillServiceImplTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info(seckills.toString());
        System.out.println(seckills.toString());
    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1003);
        System.out.println(seckill);
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1000);
        System.out.println(exposer);

    }

    @Test
    public void executeSeckill() {
        long seckillId = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        System.out.println(exposer);
        long userPhone = 12222222222L;
//        String md5 = "bf204e2683e7452aa7db1a50b5713bae";
        String md5 = DigestUtils.md5DigestAsHex((seckillId + "/thisIsASaltValue").getBytes());
        try{
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
//            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
            System.out.println(seckillExecution.toString());
        }catch (SeckillCloseException | RepeatKillException e){
            e.printStackTrace();
        }
    }

    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        System.out.println(exposer);
        long userPhone = 12222222222L;
//        String md5 = "bf204e2683e7452aa7db1a50b5713bae";
//        String md5 = DigestUtils.md5DigestAsHex((seckillId + "/thisIsASaltValue").getBytes());
        try{
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, exposer.getMd5());
//            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
            System.out.println(seckillExecution.toString());
        }catch (SeckillCloseException | RepeatKillException e){
            e.printStackTrace();
        }
    }
}
