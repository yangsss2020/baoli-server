package com.baoli.ucenter.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

/**
 * @author ys
 * @create 2020-01-19-17:53
 */

public class JwtTest {
    @Test
    public void testCreateJwt(){
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        Long ttl = 1000*10l;
        String key = "123asdf";
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("123abc")
                .setExpiration(new Date(millis + ttl))
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256, key);
        String token = jwtBuilder.compact();
        System.out.println(token);
    }
    @Test
    public void testParseJwt(){
        String key = "123asdf";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjNhYmMiLCJleHAiOjE1Nzk0OTExOTcsImlhdCI6MTU3OTQ5MTE4N30.uOvCClhTijKREikDCH61wzNb_kXiAi3_dbidso_uEuk";
        Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        String id = body.get("id").toString();
        System.out.println(id);
    }
    @Test
    public void test3() throws UnsupportedEncodingException {
        byte[] base = Base64.getDecoder().decode("eyJqdGkiOiIxMjNhYmMiLCJleHAiOjE1Nzk0OTExOTcsImlhdCI6MTU3OTQ5MTE4N30");
        System.out.println(new String(base,"utf-8"));
    }
    @Test
    public void test4() throws UnsupportedEncodingException {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IjE3Mzg0MDAwMDY5IiwiaWQiOiIxMjE4ODY3NzYwMTUxNTExMDQyIiwiZXhwIjoxNTc5NDkzNjI0fQ.V_FX9z3qL1hT2IFZB3k5kTmgzUVfcGaw1CfOReBDLlY";
//        String substring = StringUtils.substring(token, token.indexOf("."));
//        System.out.println(substring);
        String s = StringUtils.substringAfter(token, ".");
        String s1 = StringUtils.substringBeforeLast(s,".");
        System.out.println(s);
        System.out.println(s1);
//        String s2 = token.substring(token.indexOf(".")+1);
//        String s3 = StringUtils.substringBetween(token, ".", ".");
//        System.out.println(s3);
//        System.out.println(s2);
//        String s = s2.substring(s2.lastIndexOf("."));
//        System.out.println(s);
//        byte[] base = Base64.getDecoder().decode(s);
//        String s1 = new String(base, "utf-8");
//        Map<String,Object> parse = (Map<String, Object>) JSON.parse(s1);
//        System.out.println(parse.get("jti"));
    }
}
