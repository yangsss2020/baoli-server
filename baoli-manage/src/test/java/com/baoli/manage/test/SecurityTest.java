package com.baoli.manage.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ys
 * @create 2020-02-11-11:44
 */
public class SecurityTest {
    @Test
    public void test1() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("0000"));
    }
}
