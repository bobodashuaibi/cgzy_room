package com.cgzy.service.serviceImpl;

import com.cgzy.entity.t_Student;
import com.cgzy.entity.t_Teacher;
import com.cgzy.mapper.Student;
import com.cgzy.mapper.Teacher;
import com.cgzy.service.StudentService;
import com.cgzy.service.TeacherService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private Teacher teacherDao;


    /**
     * 1、老师登录、====
     * 根据老师的账号查找用户
     * @param
     * @return
     */
    @Override
    public t_Teacher selectTeacherInfo(String teacher_num) {
        return teacherDao.selectTeacherInfo(teacher_num);
    }




}
