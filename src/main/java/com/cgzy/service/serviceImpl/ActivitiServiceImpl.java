package com.cgzy.service.serviceImpl;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.cgzy.entity.*;
import com.cgzy.mapper.ActivitiDao;
import com.cgzy.mapper.Hotel;
import com.cgzy.mapper.Student;
import com.cgzy.mapper.Teacher;
import com.cgzy.service.ActivitiService;
import com.cgzy.utils.ActivitiUtil;
import com.cgzy.utils.JwtUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ActivitiServiceImpl implements ActivitiService {
    @Autowired
    private ActivitiDao activitiDao;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Teacher teacherDao;
    @Resource
    private Student studentDao;
    @Resource
    private Hotel hotelDao;

    /**
     * 在学生页面显示所有的请假信息
     */
    @RequiresAuthentication
    @Override
    public List<LeaveProcess1> selectHistoryInfo(HttpServletRequest request) throws Exception {
//        通过token获取当前用户的名字
        String usernum = JwtUtil.getUserNameByToken(request);
        System.out.println(usernum);
        Assert.notNull(usernum,"token查询出来的用户名为空");
//        通过usernum去获取用户名
        t_Student student = studentDao.getStudentInfoById(usernum);
        System.out.println(student);
        Assert.notNull(student,"进入请假信息查询页面,用户为空");
        String student_name = student.getStudent_name();
//        通过名字去历史表查询对于的processId
        List<t_History> histories = activitiDao.getHistoryInfoByUserName(student_name);
        System.out.println("hitsory"+histories);

//        创建一个list集合存查出来的processId
        List<String> processIdList = new ArrayList<>();

        for (t_History t :histories) {
            processIdList.add(t.getPROC_INST_ID_());
        }
        System.out.println("processid"+processIdList);
        System.out.println(histories.size());
        List<LeaveProcess1> leaveProcesses = new ArrayList<>();
        for (int i = 0 ; i < histories.size();i++){
            String processId = processIdList.get(i);
            System.out.println("processId====="+processId);
            LeaveProcess leaveInfoByProcessId = activitiDao.getLeaveInfoByProcessId(processId);
            System.out.println(leaveInfoByProcessId);
            //        申请的日期格式化
            Date start_time = leaveInfoByProcessId.getStart_time();
            Date end_time = leaveInfoByProcessId.getEnd_time();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String start_time1 = sdf.format(start_time);
            String end_time1 = sdf.format(end_time);
            LeaveProcess1 leaveProcess1 = new LeaveProcess1();
            leaveProcess1.setProcess_instance_id(leaveInfoByProcessId.getProcess_instance_id());
            leaveProcess1.setBusiness_key(leaveInfoByProcessId.getBusiness_key());
            leaveProcess1.setUser_id(leaveInfoByProcessId.getUser_id());
            leaveProcess1.setUser_name(leaveInfoByProcessId.getUser_name());
            leaveProcess1.setStart_time(start_time1);
            leaveProcess1.setEnd_time(end_time1);
            leaveProcess1.setLeave_type(leaveInfoByProcessId.getLeave_type());
            leaveProcess1.setReason(leaveInfoByProcessId.getReason());
            leaveProcess1.setApply_time(leaveInfoByProcessId.getApply_time());
            leaveProcess1.setState(leaveInfoByProcessId.getState());


            leaveProcesses.add(leaveProcess1);
        }

        System.out.println("最后汇总的请假信息"+leaveProcesses);
        return leaveProcesses;
    }

    /**
     * 老师页面显示请假的人的详细信息
     */
    @Override
    public List<LeaveProcess1> LeaveInfo(HttpServletRequest request, HttpServletResponse respons) throws Exception {
        //        通过token获取当前用户的名字
        String user_num = JwtUtil.getUserNameByToken(request);
        Assert.notNull(user_num,"token查询出来的用户名为空");
        t_Teacher teacher = teacherDao.selectTeacherInfo(user_num);
        Assert.notNull(teacher,"用户不存在");

        String teacher_name = teacher.getTeacher_name();

        List<Task> list = ActivitiUtil.getTaskByAssignee(teacher_name);
        List<LeaveProcess1> list1 = new ArrayList<>();
        for (Task t :list) {
            String processInstanceId = t.getProcessInstanceId();
            LeaveProcess leaveInfo = activitiDao.getLeaveInfo(processInstanceId);
            System.out.println("详细信息"+leaveInfo);
            //        申请的日期格式化
            Date start_time = leaveInfo.getStart_time();
            Date end_time = leaveInfo.getEnd_time();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String start_time1 = sdf.format(start_time);
            String end_time1 = sdf.format(end_time);
            LeaveProcess1 leaveProcess1 = new LeaveProcess1();
            leaveProcess1.setProcess_instance_id(leaveInfo.getProcess_instance_id());
            leaveProcess1.setBusiness_key(leaveInfo.getBusiness_key());
            leaveProcess1.setUser_id(leaveInfo.getUser_id());
            leaveProcess1.setUser_name(leaveInfo.getUser_name());
            leaveProcess1.setStart_time(start_time1);
            leaveProcess1.setEnd_time(end_time1);
            leaveProcess1.setLeave_type(leaveInfo.getLeave_type());
            leaveProcess1.setReason(leaveInfo.getReason());
            leaveProcess1.setApply_time(leaveInfo.getApply_time());
            leaveProcess1.setState(leaveInfo.getState());

            System.out.println(leaveProcess1);
            list1.add(leaveProcess1);
        }
        return list1;
    }

    /**
     * 老师同意审核学生信息
     */
    @Override
    public void passLeave(LeaveProcess leaveProcess, HttpServletRequest request) throws Exception {
//        通过taskId和当前用户名进行完成审核
        String teacher_num = JwtUtil.getUserNameByToken(request);
        String process_instance_id = leaveProcess.getProcess_instance_id();

        System.out.println("processId"+process_instance_id);

//            通过process查询taskId
//        通过process去获取taskId
        t_Task task1 = activitiDao.getTaskIdByProcess(process_instance_id);
        String taskId = task1.getID_();
        Date start_time = leaveProcess.getStart_time();
        Date end_time = leaveProcess.getEnd_time();

        int time = JwtUtil.differentDaysByMillisecond(start_time, end_time);
        System.out.println("计算出来的天数"+time);

        t_Teacher teacher = teacherDao.selectTeacherInfo(teacher_num);
        System.out.println(teacher);
        Assert.notNull(teacher,"查出来老师为空");
        String teacher_name = teacher.getTeacher_name();
        String job = teacher.getJob();
        if (job.equals("学生科科长")){
            ActivitiUtil.passTaskByOffice(teacher_name,taskId,"Y");
            activitiDao.uptStatePass(process_instance_id);
        }else{
            ActivitiUtil.passTaskByfudaoyuan(teacher_name,taskId,"Y",time);
            activitiDao.uptStatePass(process_instance_id);
        }
    }

    /**
     * 老师不同意审核学生信息
     */
    @Override
    public void NopassLeave(LeaveProcess leaveProcess, HttpServletRequest request) throws Exception {
//        通过taskId和当前用户名进行完成审核
        String teacher_num = JwtUtil.getUserNameByToken(request);
        String process_instance_id = leaveProcess.getProcess_instance_id();

        System.out.println("processId"+process_instance_id);

//            通过process查询taskId
//        通过process去获取taskId
        t_Task task1 = activitiDao.getTaskIdByProcess(process_instance_id);
        String taskId = task1.getID_();
        Date start_time = leaveProcess.getStart_time();
        Date end_time = leaveProcess.getEnd_time();

        int time = JwtUtil.differentDaysByMillisecond(start_time, end_time);
        System.out.println("计算出来的天数"+time);

        t_Teacher teacher = teacherDao.selectTeacherInfo(teacher_num);
        System.out.println(teacher);
        Assert.notNull(teacher,"查出来老师为空");
        String teacher_name = teacher.getTeacher_name();
        String job = teacher.getJob();
        if (job.equals("学生科科长")){
            ActivitiUtil.passTaskByOffice(teacher_name,taskId,"N");
            activitiDao.uptStateNoPass(process_instance_id);
        }else{
            ActivitiUtil.passTaskByfudaoyuan(teacher_name,taskId,"N",time);
            activitiDao.uptStateNoPass(process_instance_id);
        }
    }
//
//    /**
//     * 学生办公室 同意审核
//     */
//    @Override
//    public void passLeaveByOffice(LeaveProcess leaveProcess, HttpServletRequest request) throws Exception {
//        //        通过taskId和当前用户名进行完成审核
//        String teacher_num = JwtUtil.getUserNameByToken(request);
//        String process_instance_id = leaveProcess.getProcess_instance_id();
//
//        System.out.println("processId"+process_instance_id);
//
////            通过process查询taskId
////        通过process去获取taskId
//        t_Task task1 = activitiDao.getTaskIdByProcess(process_instance_id);
//        String taskId = task1.getID_();
//
////        最后审核流程不需要时间分支了
////        Date start_time = leaveProcess.getStart_time();
////        Date end_time = leaveProcess.getEnd_time();
////
////        int time = JwtUtil.differentDaysByMillisecond(start_time, end_time);
////        System.out.println("计算出来的天数"+time);
//
//        t_Teacher teacher = teacherDao.selectTeacherInfo(teacher_num);
//        System.out.println(teacher);
//        Assert.notNull(teacher,"查出来老师为空");
//        String teacher_name = teacher.getTeacher_name();
//
//    }
//
//    /**
//     * 学生办公室 不同意审核
//     */
//    @Override
//    public void NopassLeaveByOffice(LeaveProcess leaveProcess, HttpServletRequest request) throws Exception {
//        //        通过taskId和当前用户名进行完成审核
//        String teacher_num = JwtUtil.getUserNameByToken(request);
//        String process_instance_id = leaveProcess.getProcess_instance_id();
//
//        System.out.println("processId"+process_instance_id);
//
////            通过process查询taskId
////        通过process去获取taskId
//        t_Task task1 = activitiDao.getTaskIdByProcess(process_instance_id);
//        String taskId = task1.getID_();
//
////        最后审核流程不需要时间分支了
////        Date start_time = leaveProcess.getStart_time();
////        Date end_time = leaveProcess.getEnd_time();
////
////        int time = JwtUtil.differentDaysByMillisecond(start_time, end_time);
////        System.out.println("计算出来的天数"+time);
//
//        t_Teacher teacher = teacherDao.selectTeacherInfo(teacher_num);
//        System.out.println(teacher);
//        Assert.notNull(teacher,"查出来老师为空");
//        String teacher_name = teacher.getTeacher_name();
//        ActivitiUtil.passTaskByOffice(teacher_name,taskId,"N");
//    }

    /**
     *学生开始请假
     */
    @Override
    public boolean studentStartLeave(HttpServletRequest request, LeaveProcess leaveProcess) throws Exception {
        String userNum = jwtUtil.getUserNameByToken(request);
        t_Student student = studentDao.getStudentInfoById(userNum);
        Assert.notNull(student,"查询学生不存在");
        if (student == null){
            return false;
        }
        String username = student.getStudent_name();
        HashMap<String, String> map = new HashMap<>();
        map.put("student",username);
        map.put("fudaoyuan","何老师");
        map.put("student_office","石老师");


//        获取实例的id
        ProcessInstance processInstance = ActivitiUtil.startProcessAndAssigneeValue(map,userNum);
        String process_id = processInstance.getProcessInstanceId();
        System.out.println("processId"+process_id);
        String businessKey = processInstance.getBusinessKey();

//        申请的日期格式化
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datef = sdf.format(date);

//        通过process去获取taskId
        t_Task task1 = activitiDao.getTaskIdByProcess(process_id);
        String taskId = task1.getID_();
        System.out.println(taskId);
        LeaveProcess leaveProcess1 = new LeaveProcess();
        leaveProcess1.setProcess_instance_id(process_id);
        leaveProcess1.setBusiness_key(businessKey);
        leaveProcess1.setUser_id(student.getStudent_num());
        leaveProcess1.setUser_name(student.getStudent_name());
        leaveProcess1.setStart_time(leaveProcess.getStart_time());
        leaveProcess1.setEnd_time(leaveProcess.getEnd_time());
        leaveProcess1.setLeave_type(leaveProcess.getLeave_type());
        leaveProcess1.setReason(leaveProcess.getReason());
        leaveProcess1.setApply_time(datef);
        leaveProcess1.setState("进行中");
        System.out.println(leaveProcess1);
        try {
            activitiDao.insertLeaveInfo(leaveProcess1);
            ActivitiUtil.handleWithTaskByassignee(username,taskId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }




}
