package com.xxx.others;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xxx.dao.SeckillMapper;
import com.xxx.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @description:
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/29 22:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext-dao.xml"})
public class PageHelperTest {

    @Autowired
    private SeckillMapper seckillMapper;

    @Test
    public void query(){
        Integer pageNum = 1;
        Integer pageSize = 2;
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        List<Seckill> list1 = seckillMapper.query();
        List<Seckill> list2 = page.getResult();
        System.out.println("mapper result:" + list1.size());
        System.out.println("pagehelper result:" + list2.size());
        for (Seckill seckill : list1) {
            System.out.println(seckill);
        }
        for(int i = 0; i < Math.min(list1.size(), list2.size()); i++){
            System.out.println(list1.get(i) == list2.get(i));
        }
        System.out.println("total========" + page.getTotal());
    }
}
