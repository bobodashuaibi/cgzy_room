package com.cgzy.controller;

import cn.hutool.core.map.MapUtil;
import com.cgzy.common.lang.result;
import com.cgzy.entity.t_Admin;
import com.cgzy.entity.t_Hotel;
import com.cgzy.entity.t_Repair;
import com.cgzy.entity.t_Student;
import com.cgzy.service.AdminService;
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
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cgzy")
public class AdminController {
    @Resource
    private StudentService studentService;
    @Resource
    private AdminService adminService;
    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/user/login")
    public result login(HttpServletResponse response,@RequestBody t_Admin t_admin,HttpServletRequest request){
        System.out.println("进入了登录接口");
        String admin_num = t_admin.getAdmin_num();
        String admin_pw = t_admin.getAdmin_pw();
        System.out.println(t_admin);
        t_Admin admin = adminService.getAdminInfoById(admin_num);
        if (admin == null){
            return result.Loginfail("用户名或者密码错误");
        }
        if (!admin_pw.equals(admin.getAdmin_pw())){
            return result.Loginfail("用户名或者密码错误");
        }
        //密码正确 jwtUtils.generateToken 生成jwt Token
        String jwt = JwtUtil.sign(admin.getAdmin_num(),admin.getAdmin_pw(),"A");

        //将返回的jwt放在Header里面   传入 HttpServletResponse response
        //Header名称：Authorization
        response.setHeader("authorization",jwt);
        response.setHeader("Access-Control-Expose-Headers","authorization");

        redisUtil.set(admin_num,jwt);

        HashMap<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("admin_num",admin.getAdmin_num());
        loginInfo.put("admin_name",admin.getAdmin_name());
        //统一返回结果集
        return result.Loginsucc(jwt,loginInfo);
    }

    /**
     * 根据管理员传来的学生账号查询学生的寝室信息
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/selStudentInfo",method = RequestMethod.POST)
    public result studentInfo1(@RequestBody t_Student student,HttpServletRequest request){
        System.out.println("进入了查询学生信息接口");
        String student_num = student.getStudent_num();
        System.out.println(student_num);
//      获取刷新时间的token
        String token = request.getHeader("authorization");
        System.out.println(token);
        List<t_Student> studentInfo = adminService.getStudentInfoByNum(student_num);
        System.out.println(studentInfo);
        return result.succ(token,200,"查询成功",studentInfo);
    }

    /**
     * 根据管理员提交的信息进行录入新的寝室信息
     * @param t_hotel
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/insertHotel",method = RequestMethod.POST)
    public result insertHotel(@RequestBody t_Hotel t_hotel, HttpServletRequest request){
        System.out.println("进入了录入宿舍信息接口");
        System.out.println(t_hotel);
//      获取刷新时间的token
        String token = request.getHeader("authorization");
        System.out.println(token);
        try {
            adminService.insertHotel(t_hotel);
        }catch (Exception e){
            boolean flag =false;
            Assert.isTrue(flag,"录入失败,存在该寝室");
        }
        return result.succ(token,200,"录入成功",null);
    }

    /**
     *根据传来的宿舍名和宿舍号查询寝室数据
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/seldormitInfo",method = RequestMethod.POST)
    public result dormitInfo(@RequestBody t_Hotel t_hotel, HttpServletRequest request){
        System.out.println("进入了宿舍信息管理接口");
        System.out.println("判断输入的寝室信息是包含寝室号还是只有寝室栋数");
        System.out.println(t_hotel);
        List<t_Hotel> t_hotels;
        if (StringUtils.isEmpty(t_hotel.getRoom_number())){
            t_hotels = adminService.selectHotelInfoByHotel(t_hotel);
            System.out.println(t_hotels);
        }else{
            //根据传来的寝室信息查询入住的人数
            adminService.updateRoomInfo(t_hotel);
            t_hotels = adminService.selectHotelInfo(t_hotel);
            System.out.println(t_hotels);
        }

//      获取刷新时间的token
        String token = request.getHeader("authorization");
        System.out.println(token);

//        Map<Object, Object> map = MapUtil.builder()
//                .put("authoritys", authorityInfoArray)
//                .put("nav", navs)
//                .map();
        return result.succ(token,200,"查询成功",t_hotels);
    }

    /**
     * 管理员录入学生信息
     * @param t_student
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/student/setStuInfo",method = RequestMethod.POST)
    public result getstuInfo(@RequestBody t_Student t_student, HttpServletRequest request){
        System.out.println("进入了录入学生信息接口");
//        设置学生账号的状态
        t_student.setStatu(1);
        String student_pw = t_student.getStudent_pw();
        String s = Md5Utils.MD5(student_pw);
        t_student.setStudent_pw(s);
        System.out.println("加密了后"+t_student);
        try {
            adminService.insetStudentInfo(t_student);
        }catch (Exception e){
            e.printStackTrace();
        }
//      获取刷新时间的token
        String token = request.getHeader("authorization");
        System.out.println(token);
//        Map<Object, Object> map = MapUtil.builder()
//                .put("authoritys", authorityInfoArray)
//                .put("nav", navs)
//                .map();
        return result.succ(token,200,"录入成功",null);
    }

    /**
     * 学生信息管理
     * 管理员通过学生号或者名字查询学生的信息
     * @param t_student
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/student/stuInfo",method = RequestMethod.POST)
    public result studentInfo(@RequestBody t_Student t_student, HttpServletRequest request){
        System.out.println("进入了学生信息管理接口");
//        判断前端传过来的参数是一个还是两个
        System.out.println(t_student);
        Object student;
        if (t_student.getStudent_num().equals("")){
            student = adminService.getStudentInfoByName(t_student.getStudent_name());
        }else{
            student = adminService.getStudentInfoByNum(t_student.getStudent_num());
        }
        System.out.println(student);
        String token = request.getHeader("authorization");
        System.out.println(token);
//        Map<Object, Object> map = MapUtil.builder()
//                .put("authoritys", authorityInfoArray)
//                .put("nav", navs)
//                .map();
        return result.succ(token,200,"查询成功",student);
    }

    /**
     * 学生所有的信息查询
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/student/stuAllInfo",method = RequestMethod.POST)
    public result studentAllInfo(HttpServletRequest request){
        System.out.println("进入了查询学生所有信息接口");
        List<t_Student> studentAllInfo = adminService.getStudentAllInfo();
        String token = request.getHeader("authorization");
        System.out.println(token);
        return result.succ(token,200,"查询成功",studentAllInfo);
    }
    /**
     * 管理员管理学生信息：删除学生信息
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/student/delstuInfo",method = RequestMethod.POST)
    public result delstudentInfo(@RequestBody t_Student t_student,HttpServletRequest request){
        System.out.println("进入了删除学生信息接口");
        String student_num = t_student.getStudent_num();
        try {
            adminService.delStudentInfo(student_num);
            String token = request.getHeader("authorization");
            return result.succ(token,200,"删除成功",null);
        }catch (Exception e){
            return result.fail(400,"删除失败",null);
        }
    }

    /**
     * 管理员管理学生信息：更新学生信息
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/student/uptstuInfo",method = RequestMethod.POST)
    public result uptstudentInfo(@RequestBody t_Student t_student,HttpServletRequest request){
        System.out.println("进入了更新学生信息接口");
        try {
            adminService.uptStudentInfo(t_student);
            String token = request.getHeader("authorization");
            return result.succ(token,200,"更新成功",null);
        }catch (Exception e){
            return result.fail(400,"更新失败",null);
        }
    }

    /**
     * 寝室所有的报修信息
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/repair/allInfo",method = RequestMethod.POST)
    public result repairAllInfo(HttpServletRequest request){
        System.out.println("进入了查询所有寝室报修信息接口");
        List<t_Repair> repairAllInfo = adminService.getRepairAllInfo();
        String token = request.getHeader("authorization");
        System.out.println(token);
        return result.succ(token,200,"查询成功",repairAllInfo);
    }

    /**
     * 寝室未完成的故障的信息
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/repair/noInfo",method = RequestMethod.POST)
    public result noRepairInfo(HttpServletRequest request){
        System.out.println("进入了查询寝室未解决故障信息接口");
        List<t_Repair> noRepairInfo = adminService.getNoRepairInfo();
        String token = request.getHeader("authorization");
        System.out.println(token);
        return result.succ(token,200,"查询成功",noRepairInfo);
    }

    /**
     * 根据指定的寝室栋数和寝室号去查询单个寝室的报修情况
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/repair/roomRepair",method = RequestMethod.POST)
    public result roomRepairInfo(@RequestBody t_Repair t_repair,HttpServletRequest request){
        System.out.println("进入了查询指定寝室报修信息接口");
        List<t_Repair> repairInfo = adminService.getRepairInfo(t_repair);
        String token = request.getHeader("authorization");
        System.out.println(token);
        return result.succ(token,200,"查询成功",repairInfo);
    }

    /**
     * 管理员点击完成维修进行更新寝室报修情况
     * @param request
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(value = "/user/repair/succRepair",method = RequestMethod.POST)
    public result succRepairInfo(@RequestBody t_Repair t_repair,HttpServletRequest request){
        System.out.println("进入了完成寝室报修信息接口");
        try {
            adminService.succRepairInfo(t_repair);
            String token = request.getHeader("authorization");
            System.out.println(token);
            return result.succ(token,200,"更新成功",null);
        }catch (Exception e){
            return result.fail(400,"更新失败",null);
        }
    }

    /**
     *   管理员查询所有寝室信息
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/user/hotelAllInfo",method = RequestMethod.POST)
    public result hotelAllInfo(HttpServletRequest request) throws Exception {
        System.out.println("管理员查询所有寝室信息");
        try {
            List<t_Hotel> t_hotels = adminService.hotelAllInfo();
            return result.succ(200,"查询成功",t_hotels);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.succ(400,"查询失败",null);
    }

    /**
     *   管理员删除寝室信息
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/user/delHotelInfo",method = RequestMethod.POST)
    public result delHotelInfo(@RequestBody t_Hotel t_hotel, HttpServletRequest request) throws Exception {
        System.out.println("管理员删除指定寝室信息");
        try {
            System.out.println(t_hotel);
            adminService.delhotelInfo(t_hotel);
            return result.succ(200,"删除成功",null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.succ(400,"删除失败",null);
    }

    /**
     *   超级管理员添加管理员
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/user/admin/add",method = RequestMethod.POST)
    public result succAdminAddInfo(@RequestBody t_Admin t_admin, HttpServletRequest request) throws Exception {
        System.out.println("超级管理员添加管理员信息");
        try {
            t_admin.setAdmin_room("2A");
            adminService.insertAdmin(t_admin);
            return result.succ(200,"添加成功",null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.succ(400,"添加失败",null);
    }

    /**
     *   超级管理员管理查询管理员
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/user/selectAdminInfo",method = RequestMethod.POST)
    public result AdminInfo(){
        System.out.println("超级管理员查询管理员信息");
        try {
            List<t_Admin> adminInfo = adminService.getAdminInfo();
            return result.succ(200,"查询成功",adminInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.succ(400,"添加失败",null);
    }

    /**
     *   超级管理员删除查询管理员
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/user/delAdminInfo",method = RequestMethod.POST)
    public result AdminInfo(@RequestBody t_Admin t_admin){
        System.out.println("超级管理员查询管理员信息");
        try {
            adminService.delAdmin(t_admin);
            return result.succ(200,"删除成功",null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.succ(400,"添加失败",null);
    }

    /**
     *   超级管理员更改管理员信息
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/user/uptAdminInfo",method = RequestMethod.POST)
    public result uptAdminInfo(@RequestBody t_Admin t_admin){
        System.out.println("超级管理员更新管理员信息");
        try {
            adminService.uptAdmin(t_admin);
            return result.succ(200,"更新成功",null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.succ(400,"更新失败",null);
    }

    /**
     *   超级管理员指定账号查询管理员
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/user/selectAdminById",method = RequestMethod.POST)
    public result AdminInfoById(@RequestBody t_Admin t_admin){
        System.out.println("超级管理员查询指定管理员信息");
        try {
            List<t_Admin> info = adminService.getAdminById(t_admin.getAdmin_num());
            return result.succ(200,"查询成功",info);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.succ(400,"查询失败",null);
    }


}
