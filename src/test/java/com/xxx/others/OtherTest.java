package com.xxx.others;

import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/27 17:53
 */

public class OtherTest {

    @Test
    public void localDateTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.toLocalTime());
    }
}
