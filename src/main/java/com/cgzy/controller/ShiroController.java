package com.cgzy.controller;


import com.cgzy.common.lang.result;
import com.cgzy.utils.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于用户登录后的登出操作
 * 清楚token和刷新token
 *
 */

@RequestMapping("/cgzy")
@RestController
public class ShiroController {

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/user/toLogin")
    public result toLogin(int flag){
        return result.fail(201,"未登录",null);
    }

    @RequiresAuthentication
    @RequestMapping("/sys/logout")
    public result logout(@RequestHeader("token") String token){
        SecurityUtils.getSubject().logout();
        this.redisUtil.del(token);
        return result.Logoutsucc();
    }


}
