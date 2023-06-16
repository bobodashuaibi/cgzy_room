package com.cgzy.service.serviceImpl;

import com.cgzy.entity.t_Repair;
import com.cgzy.entity.t_Student;
import com.cgzy.entity.t_Student_Mail;
import com.cgzy.mapper.Hotel;
import com.cgzy.mapper.Repair;
import com.cgzy.mapper.Student;
import com.cgzy.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private Student studentDao;
    @Resource
    private Repair repairDao;

    /**
     * 1、学生登录、====
     * 根据学生的账号查找用户
     * @param student_num
     * @return
     */
    @Override
    public t_Student getStudentInfoById(String student_num) {
        return studentDao.getStudentInfoById(student_num);
    }

    @Override
    public void insertRepairInfo(t_Repair t_repair) {
        repairDao.insertRepairInfo(t_repair);
    }

    @Override
    public void uptPwById(String student_num,String student_pw) {
        studentDao.uptPwById(student_num,student_pw);
    }

    @Override
    public t_Student_Mail studentNumByMail(String mail) {
        return studentDao.studentNumByMail(mail);
    }
}
