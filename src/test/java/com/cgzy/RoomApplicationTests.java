package com.cgzy;

import com.cgzy.entity.*;
import com.cgzy.mapper.*;
import com.cgzy.service.ActivitiService;
import com.cgzy.service.AdminService;
import com.cgzy.service.MailService;
import com.cgzy.utils.ActivitiUtil;
import com.cgzy.utils.JwtUtil;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
class RoomApplicationTests {
    @Resource
    private Student student;
    @Resource
    private Hotel hotel;
    @Resource
    private AdminService adminService;
    @Resource
    private Repair repair;
    @Resource
    private Teacher teacherDao;
    @Resource
    private ActivitiService activitiService;
    @Resource
    private ActivitiDao activitiDao;
    @Resource
    private MailService mailService;
//    @Autowired
//    private Producer producer;

    @Test
    void contextLoads() throws Exception {

//        AntPathMatcher antPathMatcher = new AntPathMatcher(File.separator);

//        boolean match = antPathMatcher.match("/user/**", "/user/selInfo/201961021203134/");
//        System.out.println(match);
//        String sign = JwtUtil.sign("admin", "admin", "A");
//        System.out.println("token"+sign);
//        String userType = JwtUtil.getUserType(sign);
//        System.out.println("类型"+userType);
//
//        boolean verify = JwtUtil.verify(sign, "admin", "admin","A");
//        System.out.println(verify);

//        t_Hotel t_hotel = new t_Hotel();
//        t_hotel.setHotel_number("3B");
//        t_hotel.setRoom_number("666");
//        t_hotel.setBed_number("6");
//        try {
//            hotel.insertHotel(t_hotel);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        t_Hotel t_hotel = new t_Hotel();
//        t_hotel.setHotel_number("3A");
//        t_hotel.setRoom_number("333");

//        List<t_Hotel> t_hotels = adminService.selectHotelInfoByHotel(t_hotel);
//        System.out.println(t_hotels);

//
//        int i = student.selectRoomByPeople(t_hotel);
//        System.out.println(i);

//        hotel.updateRoomPeople(4,t_hotel);

//        adminService.updateRoomInfo(t_hotel);

//        List<t_Repair> noRepairInfo = repair.getNoRepairInfo();
//        System.out.println(noRepairInfo);

//        t_Teacher teacher = teacherDao.selectTeacherInfo("002");
//        System.out.println(teacher);
//
////        学生主页进行查询自己的任务
//        List<LeaveProcess> wanglaos = activitiService.studentLeaverInfo1("王博");
//        System.out.println(wanglaos);

//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        List<Task> list=processEngine.getTaskService()
//                .createTaskQuery()
//                .taskAssignee("罗佳")
//                .list();
//        for (Task task:list
//             ) {
//            System.out.println(task.getBusinessKey());
//            System.out.println(task.getName());
//        }

//        List<Task> list = ActivitiUtil.getTaskByAssignee("何老师");
//        List<LeaveProcess> list1 = new ArrayList<>();
//        for (Task t :list) {
//            String businessKey = t.getBusinessKey();
//            String task_id = t.getId();
//            LeaveProcess leaveInfo = activitiDao.getLeaveInfo(businessKey);
//            list1.add(leaveInfo);
//        }
//        System.out.println(list1);


//        //1. 通过工具类（ProcessEngines）获取流程引擎实例
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        //2. 得到TaskService实例
//        TaskService taskService = processEngine.getTaskService();

//        //3. 根据流程定义的key，负责人assignee来实现当前用户的任务列表查询
//        List<Task> list = taskService.createTaskQuery()//
//                .taskAssignee(username)//指定个人任务查询
//                .orderByTaskCreateTime().asc()//
//                .list();

//        List<t_History> histories = activitiDao.getHistoryInfoByUserName("罗佳");
//        System.out.println(histories);
//        LeaveProcess leaveInfoByProcessId = activitiDao.getLeaveInfoByProcessId("5001");
//        System.out.println(leaveInfoByProcessId);
//        ActivitiUtil.passTaskByfudaoyuan("何老师","17511","y",5);
        //获取日期
        //导 import java.util.Date; 下的包
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String datef = sdf.format(date);
//        System.out.println("格式化后的日期：" + datef);
        //格式化后的日期：2019-11-29

//        t_Hotel t_hotel = new t_Hotel();
//        t_hotel.setHotel_number("1A");
//        t_hotel.setRoom_number("111");
//        t_hotel.setBed_number("6");
//        adminService.insertHotel(t_hotel);
//        List<t_Student> list = adminService.getStudentInfoByName("王博");
//        System.out.println(list);

//        mailService.sendMail("1975769453@qq.com","123");

//        String text = producer.createText();
//        System.out.println(text);

        Random r = new Random(1);
        for(int i=0 ; i<5 ;  i++) {
            int ran1 = r.nextInt(10000);
            System.out.println(ran1);
        }

    }


}
