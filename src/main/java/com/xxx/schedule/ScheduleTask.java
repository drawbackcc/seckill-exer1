package com.xxx.schedule;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/28 5:25
 */
@Component
//@EnableScheduling
public class ScheduleTask {
    // 每隔5s执行一次
    @Scheduled(cron="0/5 * * * * ?")
//    @Scheduled
    public void hello() {
        try {
            TimeUnit.SECONDS.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date())+"  S每天起床第一句，先给自己打打气~~" + Thread.currentThread().getName());
    }
}
