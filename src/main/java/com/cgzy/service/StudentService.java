package com.cgzy.service;


import com.cgzy.entity.t_Repair;
import com.cgzy.entity.t_Student;
import com.cgzy.entity.t_Student_Mail;

public interface StudentService {

    t_Student getStudentInfoById(String student_num);

    /**
     * 学生插入报修信息
     *
     */
    void insertRepairInfo(t_Repair t_repair);

    void uptPwById(String student_num,String student_pw);

    t_Student_Mail studentNumByMail(String mail);
}
