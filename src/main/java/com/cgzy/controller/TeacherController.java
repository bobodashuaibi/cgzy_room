package com.cgzy.controller;

import com.cgzy.common.lang.result;
import com.cgzy.entity.*;
import com.cgzy.mapper.Teacher;
import com.cgzy.service.ActivitiService;
import com.cgzy.service.AdminService;
import com.cgzy.service.StudentService;
import com.cgzy.service.TeacherService;
import com.cgzy.utils.JwtUtil;
import com.cgzy.utils.Md5Utils;
import com.cgzy.utils.RedisUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cgzy")
public class TeacherController {
    @Resource
    private TeacherService teacherService;
    @Resource
    private AdminService adminService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ActivitiService activitiService;

    /**
     * 老师登录接口
     * @param teacher
     * @param response
     * @return
     */
    @RequestMapping(value = "/tea/login",method = RequestMethod.POST)
    public result login(@RequestBody t_Teacher teacher, HttpServletResponse response,HttpServletRequest request){
        System.out.println("进入了老师登录接口");
        String teacher_num = teacher.getTeacher_num();
        String password = teacher.getPassword();
//            查出来老师信息对象
        t_Teacher teacher1 = teacherService.selectTeacherInfo(teacher_num);
        if (teacher1 == null){
            return result.Loginfail("用户名或者密码错误");
        }

        if (!teacher1.getPassword().equals(password)){
            return result.Loginfail("用户名或者密码错误");
        }
        //密码正确 jwtUtils.generateToken 生成jwt Token
        String jwt = JwtUtil.sign(teacher1.getTeacher_num(),password,"T");

        //将返回的jwt放在Header里面   传入 HttpServletResponse response
        //Header名称：Authorization
        response.setHeader("authorization",jwt);
        response.setHeader("Access-Control-Expose-Headers","authorization");

        redisUtil.set(teacher_num,jwt);

        HashMap<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("teacher_num",teacher1.getTeacher_num());
        loginInfo.put("teacher_name",teacher1.getTeacher_name());
        //统一返回结果集
        return result.Loginsucc(jwt,loginInfo);
    }

    /**
     * 老师进入审核页面，显示当前需要处理的任务
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/tea/allInfo",method = RequestMethod.POST)
    public result selectInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<LeaveProcess1> leaveProcesses = activitiService.LeaveInfo(request, response);
        return result.succ(200,"查询成功",leaveProcesses);
    }


}
