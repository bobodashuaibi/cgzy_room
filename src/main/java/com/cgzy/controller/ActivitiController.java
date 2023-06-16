package com.cgzy.controller;


import com.cgzy.common.lang.result;
import com.cgzy.entity.LeaveProcess;
import com.cgzy.entity.LeaveProcess1;
import com.cgzy.entity.t_Student;
import com.cgzy.entity.t_Teacher;
import com.cgzy.service.ActivitiService;
import com.cgzy.service.StudentService;
import com.cgzy.service.TeacherService;
import com.cgzy.utils.JwtUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cgzy")
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;
    @Resource
    private StudentService studentService;

    /**
     * 显示请假的历史信息
     * @return
     */
    @RequestMapping(value =  "/stu/historyInfo",method = RequestMethod.POST)
    public result selectHistoryInfo(HttpServletRequest request) throws Exception {
        List<LeaveProcess1> leaveProcesses = activitiService.selectHistoryInfo(request);
        return result.succ(200,"查询成功",leaveProcesses);
    }


    /**
     * 老师进入审核页面，显示当前需要处理的任务
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/tea/actAllInfo",method = RequestMethod.POST)
    public result teaselectInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("进入了老师审核页面");
        List<LeaveProcess1> leaveProcesses = activitiService.LeaveInfo(request, response);
        return result.succ(200,"查询成功",leaveProcesses);
    }

    /**
     *   老师同意审核
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/tea/activitiPass",method = RequestMethod.POST)
    public result teacherPass(@RequestBody  LeaveProcess1 leaveProcess,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String start_time = leaveProcess.getStart_time();
        String end_time = leaveProcess.getEnd_time();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime= sdf.parse(start_time);
        Date endTime= sdf.parse(end_time);

        LeaveProcess leaveProcess1 = new LeaveProcess();
        leaveProcess1.setProcess_instance_id(leaveProcess.getProcess_instance_id());
        leaveProcess1.setStart_time(startTime);
        leaveProcess1.setEnd_time(endTime);


        try {
            System.out.println("老师同意通过");

            activitiService.passLeave(leaveProcess1,request);
            String token = request.getHeader("authorization");
            return result.passLeave(token,200,"同意通过");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.fail(400,"错误",null);
    }

    /**
     *   老师不同意审核
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/tea/activitiNoPass",method = RequestMethod.POST)
    public result teacherNoPass(@RequestBody LeaveProcess1 leaveProcess,HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("老师不同意通过");
        String start_time = leaveProcess.getStart_time();
        String end_time = leaveProcess.getEnd_time();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime= sdf.parse(start_time);
        Date endTime= sdf.parse(end_time);

        LeaveProcess leaveProcess1 = new LeaveProcess();
        leaveProcess1.setProcess_instance_id(leaveProcess.getProcess_instance_id());
        leaveProcess1.setStart_time(startTime);
        leaveProcess1.setEnd_time(endTime);
        try {
            activitiService.NopassLeave(leaveProcess1,request);
        }catch (Exception e){
            e.printStackTrace();
        }
        String token = request.getHeader("authorization");
        return result.passLeave(token,400,"不同意通过");
    }

    /**
     *   学生查询寝室信息
     */
    @RequiresAuthentication
    @RequestMapping(value =  "/stu/hotelInfo",method = RequestMethod.POST)
    public result hotelInfoByStudent(HttpServletRequest request) throws Exception {
        System.out.println("学生查询寝室信息");
        try {
            String student_num = JwtUtil.getUserNameByToken(request);
            t_Student student = studentService.getStudentInfoById(student_num);
            t_Student student1 = new t_Student();
            student1.setHotel_number(student.getHotel_number());
            student1.setRoom_number(student.getRoom_number());
            return result.succ(200,"查询成功",student1);
        }catch (Exception e){
            e.printStackTrace();
        }
        String token = request.getHeader("authorization");
        return result.succ(400,"查询失败",null);
    }



}
