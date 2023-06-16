package com.cgzy.service;


import com.cgzy.entity.LeaveProcess;
import com.cgzy.entity.LeaveProcess1;
import com.cgzy.entity.t_Hotel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ActivitiService {

    /**
     * 请假页面显示自己申请了的
     */
//    List<LeaveProcess> studentLeaverInfo(HttpServletRequest request, HttpServletResponse respons) throws Exception;
//
//    List<LeaveProcess> studentLeaverInfo1(String username) throws Exception;

    /**
     * 用户查看历史信息
     */
    List<LeaveProcess1> selectHistoryInfo(HttpServletRequest request) throws Exception;



    /**
     * 老师页面显示请假的人的详细信息
     */
    List<LeaveProcess1> LeaveInfo(HttpServletRequest request, HttpServletResponse respons) throws Exception;

    /**
     * 老师   同意学生的请假
     */
    void passLeave(LeaveProcess leaveProcess,HttpServletRequest request) throws Exception;

    /**
     * 老师  不同意学生的请假
     */
    void NopassLeave(LeaveProcess leaveProcess,HttpServletRequest request) throws Exception;

//    /**
//     * 学生办公室 同意学生的请假
//     */
//    void passLeaveByOffice(LeaveProcess leaveProcess,HttpServletRequest request) throws Exception;
//
//    /**
//     * 学生办公室 同意学生的请假
//     */
//    void NopassLeaveByOffice(LeaveProcess leaveProcess,HttpServletRequest request) throws Exception;

    /**
     * 学生提交申请
     */
    boolean studentStartLeave(HttpServletRequest request,LeaveProcess leaveProcess) throws Exception;



}
