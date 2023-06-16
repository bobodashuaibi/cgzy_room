package com.cgzy.utils;

import cn.hutool.jwt.Claims;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.cgzy.mapper.Student;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static java.security.KeyRep.Type.SECRET;

@Component
public class JwtUtil {


    // Token过期时间30分钟（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准）
    public static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

    @Resource
    private Student student;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret,String user_type) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("user_num", username)
                    .withClaim("user_type", user_type)
                    .build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }


    /**
     * 获得token中的信息,无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("user_num").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的type信息,无需secret解密也能获得
     *
     * @return token中包含的用户的类型
     */
    public static String getUserType(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("user_type").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret,String user_type) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("user_num", username)
                .withClaim("user_type", user_type)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 根据request中的token获取用户账号
     *
     * @param request
     * @return
     */
    public static String getUserNameByToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("authorization");
        String usernum = getUsername(token);
        if (StringUtils.isEmpty(usernum)) {
            throw new Exception("未获取到用户");
        }
        return usernum;
    }

    public static void main(String[] args) throws Exception{
//		 /*String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjUzMzY1MTMsInVzZXJuYW1lIjoiYWRtaW4ifQ.xjhud_tWCNYBOg_aRlMgOdlZoWFFKB_givNElHNw3X0";
//		 System.out.println(JwtUtil.getUsername(token));*/
//
//        String token = JwtUtil.sign("admin", "123456");
//        System.out.println(token);
    }



    /**
     * 抽出方法，专门用来  token进行查询用户id
     */
//
//    public t_student getUserNameByToken(HttpServletRequest request, HttpServletResponse response){
//        //        解析前端传过来的token
//        String token = request.getHeader("Authorization");
//        String user_num = JwtUtil.getUsername(token);
//        System.out.println("author取出来的用户账号"+user_num);
//
//        t_Student student = userDao.SelectUserById(user_num);
//        if (StringUtils.isEmpty(student)){
//            return null;
//        }else {
//            return student;
//        }
//    }

//    public t_Teacher getTeacherNameByToken(HttpServletRequest request, HttpServletResponse response){
//        //        解析前端传过来的token
//        String token = request.getHeader("Authorization");
//        String user_num = JwtUtil.getUsername(token);
//        System.out.println("author取出来的用户账号"+user_num);
//
//        t_Teacher teacher = userDao.SelectTeacher(user_num);
//        if (StringUtils.isEmpty(teacher)){
//            return null;
//        }else {
//            return teacher;
//        }
//    }


    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

}
