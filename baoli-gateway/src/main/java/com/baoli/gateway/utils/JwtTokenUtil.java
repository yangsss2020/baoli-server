package com.baoli.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.baoli.ums.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-19-18:24
 */
//@Component
@Configuration
public class JwtTokenUtil implements Serializable {
    @Value("${baoli.jwt.key}")
    private String key;
    @Value("${baoli.jwt.expiration}")
    private Integer expiration;

    /**
     * 生成token
     *
     * @param claims
     * @return
     */
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(DateTime.now().plusHours(expiration).toDate())
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成token
     *
     * @param member
     * @return
     */
    public String generateToken(Member member) {
        Map<String, Object> map = new HashMap<>();
        BeanUtils.copyProperties(member, map);
        map.put("id", member.getId());
        map.put("phone", member.getPhone());
        return this.generateToken(map);
    }

    public Member getMemberFormToken(String token) {
        Member member = new Member();
        Claims claims = this.getClaimsFromToken(token);
        if (CollectionUtils.isEmpty(claims)) {
            return null;
        }
        String id = claims.get("id").toString();
        String phone = claims.get("phone").toString();
        member.setId(id);
        member.setPhone(phone);
        return member;
    }

    public Boolean isTokenValid(String token) {
        try {
            Claims claims = this.getClaimsFromToken(token);
            Date date = claims.getExpiration();
            return new Date().before(date);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {

        return null;
    }

    /**
     * 根据过期token获取member
     *
     * @param token
     * @return
     */
    public Member getMemberFromexpiredToken(String token) {
        String s = StringUtils.substringAfter(token, ".");
        String s1 = StringUtils.substringBeforeLast(s,".");
        Member member = new Member();
        byte[] base = Base64.getDecoder().decode(s1);
        String jsonStr = null;
        try {
            jsonStr = new String(base, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        Map<String, Object> map = (Map<String, Object>) JSON.parse(jsonStr);
        if (CollectionUtils.isEmpty(map)) {
            return null;
        } else {
            for (String entry : map.keySet()) {
                if (entry.equals("id")) {
                    member.setId(map.get(entry).toString());
                }
                if (entry.equals("phone")) {
                    member.setPhone(map.get(entry).toString());
                }
            }
        }
        return member;
    }

    /**
     * 验证token
     *
     * @param token
     * @param userId
     * @return
     */
    public Boolean validateToken(String token, String userId) {
        Claims claims = this.getClaimsFromToken(token);
        if (claims.isEmpty()) {
            return false;
        }
        String id = claims.get("id").toString();
        if (StringUtils.equals(userId, id)) {
            return true;
        } else {
            return false;
        }
    }
}
