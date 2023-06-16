package com.cgzy.mapper;

import com.cgzy.entity.t_Hotel;
import com.cgzy.entity.t_Student;
import com.cgzy.entity.t_Student_Mail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Student {
    /**
     * 根据学生的学号查询信息
     * @param student_num
     * @return
     */
    t_Student getStudentInfoById(String student_num);

    /**
     * 管理员查询学神信息
     */
    List<t_Student> getStudentInfoByNum(String student_num);

    /**
     * 管理员录入学生的信息
     * @param student
     */
    void insertStudentInfo(t_Student student);

    /**
     * 用于更新寝室住的人数，进行查询指定寝室有多少人
     */
    int selectRoomByPeople(t_Hotel t_hotel);

    /**
     * 管理员根据学生名字查询
     */
    List<t_Student> getStudentInfoByName(String studentName);

    /**
     * 获取所有的学生信息
     * @return
     */
    List<t_Student> getStudentAllInfo();

    /**
     * 管理员管理学生信息：删除学生信息
     */
    void delStudentInfo(String student_number);

    /**
     * 管理员管理学生信息：更新学生信息
     */
    void uptStudentInfo(t_Student t_Student);

    void uptPwById(String student_num,String student_pw);

    t_Student_Mail studentNumByMail(String mail);




}
