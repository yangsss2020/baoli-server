package com.baoli.ucenter.test;

import com.baoli.ums.entity.Member;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-19-19:37
 */
public class MyTest {
    @Test
    public void test1(){
        Member member = new Member();
        member.setUsername("14324");
        member.setPhone("13101060040");
        Map<String,Object> map = new HashMap<>();
        BeanUtils.copyProperties(member,map);
        for (Object value : map.values()) {
            System.out.println(value);
        }

    }
    @Test
    public void test2(){
        Date old = new Date();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date newDate = new Date();
        System.out.println( old.before(newDate));
    }
    @Test
    public void test3(){
        BigDecimal num1 = new BigDecimal(10);
        BigDecimal num2 = new BigDecimal(20);
//        num1.add(num2);
//        num1 = num2;
//        System.out.println(num1);
        int i = num2.compareTo(num1);
        System.out.println(i);
    }
}
