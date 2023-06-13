package com.resume.base.utils;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;
@Slf4j
public class JwtUtil {
    //过期时间
    private static final long access_expire = 24*60*60*1000;
    private static final long refresh_expire = 2*24*60*60*1000;
    //秘钥
    private static final String secret = "pp_tyy_lyh_zjz";
    //加密
    public static String createAccessToken(Long userId,Long companyId){
        JwtBuilder jwtBuilder= Jwts.builder();
        return jwtBuilder
                //头信息header
                .setHeaderParam("typ","JWT")//Token类型
                .setHeaderParam("alg","HS256")//加密算法
                //载荷payload,信息
                .claim("userId",""+userId)
                .claim("companyId",""+companyId)
                //主题
                .setSubject("access_token")
                //有效时间
                .setExpiration(new Date(System.currentTimeMillis()+access_expire))
                //ID
                .setId(UUID.randomUUID().toString())
                //signature签名
                .signWith(SignatureAlgorithm.HS256,secret)
                //用.拼接
                .compact();
    }
    public static String createRefreshToken(){
        JwtBuilder jwtBuilder= Jwts.builder();
        return jwtBuilder
                //头信息header
                .setHeaderParam("typ","JWT")//Token类型
                .setHeaderParam("alg","HS256")//加密算法
                //主题
                .setSubject("refresh_token")
                //有效时间
                .setExpiration(new Date(System.currentTimeMillis()+refresh_expire))
                //ID
                .setId(UUID.randomUUID().toString())
                //signature签名
                .signWith(SignatureAlgorithm.HS256,secret)
                //用.拼接
                .compact();
    }
    public static String getUserId(String token){
        JwtParser jwtParser=Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(secret).parseClaimsJws(token);
        Claims claims=claimsJws.getBody();
        return (String)claims.get("userId");
    }
    public static String getCompanyId(String token){
        JwtParser jwtParser=Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(secret).parseClaimsJws(token);
        Claims claims=claimsJws.getBody();
        return (String)claims.get("companyId");
    }
    public static String checkToken(String token){
         //确保token此时一定不为空
        JwtParser jwtParser=Jwts.parser();
        try {
            Jws<Claims> claimsJws = jwtParser.setSigningKey(secret).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.error("JWT过期：", e);
            return Constant.EXPIRED_JWT_EXCEPTION;
        } catch (UnsupportedJwtException e) {
            log.error("不支持的JWT：", e);
            return Constant.UNSUPPORTED_JWT_EXCEPTION;
        } catch (MalformedJwtException e) {
            log.error("JWT格式错误：", e);
            return Constant.MALFORMED_JWT_EXCEPTION;
        } catch (SignatureException e) {
            log.error("签名异常：", e);
            return Constant.SIGNATURE_EXCEPTION;
        } catch (IllegalArgumentException e) {
            log.error("非法请求：", e);
            return Constant.ILLEGAL_ARGUMENT_EXCEPTION;
        } catch (Exception e) {
            log.error("解析异常：", e);
            return Constant.EXCEPTION;
        }
        return Constant.RIGHT_TOKEN;
    }
}
