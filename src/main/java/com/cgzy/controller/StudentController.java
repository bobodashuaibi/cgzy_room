package com.cgzy.controller;

import com.cgzy.common.lang.result;
import com.cgzy.entity.*;
import com.cgzy.service.ActivitiService;
import com.cgzy.service.MailService;
import com.cgzy.service.StudentService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@RestController
@RequestMapping("/cgzy")
public class StudentController {
    @Resource
    private StudentService studentService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ActivitiService activitiService;
    @Resource
    private MailService mailService;



    @RequestMapping("/stu/login")
    public result login(@RequestBody t_Student t_student, HttpServletResponse response){
        System.out.println("进入了登录接口");
        String student_num = t_student.getStudent_num();
        String student_pw = t_student.getStudent_pw();
        t_Student student = studentService.getStudentInfoById(student_num);
        System.out.println(student);
        if (student == null){
            return result.Loginfail("用户名或者密码错误");
        }
        System.out.println("===================");
        System.out.println(student_pw);
        String jm = Md5Utils.MD5(student_pw);
        System.out.println(jm);
        if (!student.getStudent_pw().equals(jm)){
            return result.Loginfail("用户名或者密码错误");
        }
        //密码正确 jwtUtils.generateToken 生成jwt Token
        String jwt = JwtUtil.sign(student.getStudent_num(),student.getStudent_pw(),"S");

        //将返回的jwt放在Header里面   传入 HttpServletResponse response
        //Header名称：Authorization
        response.setHeader("authorization",jwt);
        response.setHeader("Access-Control-Expose-Headers","authorization");

        redisUtil.set(student_num,jwt);

        HashMap<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("studentNum",student.getStudent_num());
        loginInfo.put("studentName",student.getStudent_name());
        //统一返回结果集
        return result.Loginsucc(jwt,loginInfo);
    }

    /**
     * 学生发送邮箱请求
     */
    @RequestMapping("/stu/pass")
    public result passPwInfo(@RequestBody  t_Student_Mail t_student_mail) {
//        查询是否邮箱存在
        t_Student_Mail t_student_mail1 = studentService.studentNumByMail(t_student_mail.getMail_num());
        System.out.println("t"+t_student_mail1);
        if (StringUtils.isEmpty(t_student_mail1)){
            return result.fail(200,"发送失败，邮箱不存在",null);
        }else {
            Random r = new Random();
            int sign = r.nextInt(10000);
            mailService.sendMail(t_student_mail.getMail_num(),sign);
            redisUtil.hset("mail",t_student_mail1.getMail_num(),sign,120);
            return result.succ(200,"发送成功",null);
        }
    }
    /**
     * 校验邮箱验证码完成修改
     */
    @RequestMapping("/stu/mail")
    public result passPwByMail(@RequestBody userDto userDto) {
        System.out.println(userDto);
        String mail_num = userDto.getMail_num();
        String mail = userDto.getMail();
        String student_pw = userDto.getStudent_pw();
        System.out.println("进入");
        //        查询是否redis邮箱存在
        String mail1 = redisUtil.hget("mail", mail_num).toString();
        System.out.println("=========");
        if (mail1.equals(mail)){
            t_Student_Mail t_student_mail1 = studentService.studentNumByMail(mail_num);
            String student_num = t_student_mail1.getStudent_num();
            String s = Md5Utils.MD5(student_pw);
            studentService.uptPwById(student_num,s);
            redisUtil.hdel("mail",mail_num);
            return result.succ(200,"修改成功",null);
        }else{
            return result.fail(400,"验证码失效或者错误",null);
        }
    }



    /**
     * 学生提交请假申请
     */
    @RequiresAuthentication
    //    @ResponseBody 用来springmvc进行设置网页头，json返回
//    @ResponseBody
    @RequestMapping("/stu/submitInfo")
    public result submitLeaveInfo(@RequestBody  LeaveProcess leaveProcess, HttpServletRequest request) {
        try {
            activitiService.studentStartLeave(request,leaveProcess);
            String token = request.getHeader("authorization");
            return result.succ(token,200,"申请成功",null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.fail(400,"申请失败",null);
    }

    /**
     * 学生提交报修申请
     */
    @RequiresAuthentication
    @RequestMapping("/stu/submitRepairInfo")
    public result submitRepairInfo(@RequestBody t_Repair repair, HttpServletRequest request) {
        System.out.println("进入报修");
        try {
            repair.setStatu("维修中");
            studentService.insertRepairInfo(repair);
            String token = request.getHeader("authorization");
            return result.succ(token,200,"报修成功",null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.fail(400,"申请失败",null);
    }

    /**
     * 学生查看个人 信息
     */
    @RequiresAuthentication
    @RequestMapping("/stu/stuInfo")
    public result studentInfo(HttpServletRequest request) throws Exception {
        String userNum = JwtUtil.getUserNameByToken(request);
        t_Student studentInfo = studentService.getStudentInfoById(userNum);
        studentInfo.setStudent_pw("");
        ArrayList<t_Student> arr = new ArrayList<>();
        arr.add(studentInfo);

        if (StringUtils.isEmpty(studentInfo)){
            return result.fail(400,"查无信息，出错",null);
        }else{
            String token = request.getHeader("authorization");
            return result.succ(token,200,"查询成功",arr);
        }

    }

    /**
     * 学生修改密码
     */
    @RequiresAuthentication
    @RequestMapping("/stu/uptpw")
    public result studentPwUpt(@RequestBody t_Student student,HttpServletRequest request) throws Exception {
        String userNum = JwtUtil.getUserNameByToken(request);
        String student_pw = student.getStudent_pw();
        try {
            String token = request.getHeader("authorization");
            String s = Md5Utils.MD5(student_pw);

            studentService.uptPwById(userNum,s);
            return result.succ(token,200,"修改成功",null);
        }catch (Exception e){
            return result.fail(400,"查无信息，出错",null);
        }
    }
}
