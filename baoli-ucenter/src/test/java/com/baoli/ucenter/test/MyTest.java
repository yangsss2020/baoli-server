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
    public void test1() {
        Member member = new Member();
        member.setUsername("14324");
        member.setPhone("13101060040");
        Map<String, Object> map = new HashMap<>();
        BeanUtils.copyProperties(member, map);
        for (Object value : map.values()) {
            System.out.println(value);
        }

    }
    @Test
    public void testStr(){
        String url = "13340119244";
        String s = url.substring(7);
        String s1 = url.substring(0, 3);
        System.out.println(s);
        System.out.println(s1);
    }

    @Test
    public void test2() {
        Date old = new Date();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date newDate = new Date();
        System.out.println(old.before(newDate));
    }

    @Test
    public void test3() {
        BigDecimal num1 = new BigDecimal(10);
        BigDecimal num2 = new BigDecimal(20);
//        num1.add(num2);
//        num1 = num2;
//        System.out.println(num1);
        int i = num2.compareTo(num1);
        System.out.println(i);
    }

    @Test
    public void test4() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "");
        System.out.println(map.get("id").toString());
        System.out.println(map.get("te"));
    }

    @Test
    public void test5() {
        Integer num = 200;
        Integer num1 = 200;
        System.out.println(num.intValue()==num1.intValue());
//        change(num);
//        System.out.println(num);  //1 ,hello,2,[2,2,3,4,5],11
    }

    void change(int i) {
        i+=1;
    }
}
