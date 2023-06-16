package com.cgzy.service;

import com.cgzy.entity.t_Admin;
import com.cgzy.entity.t_Hotel;
import com.cgzy.entity.t_Repair;
import com.cgzy.entity.t_Student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AdminService {

    /**
     * 根据管理员账号查询账户
     * @param admin_num
     * @return
     */
    t_Admin getAdminInfoById(String admin_num);

    List<t_Admin> getAdminById(String admin_num);

    /**
     * 超级管理员查询数据
     * @return
     */
    List<t_Admin> getAdminInfo();



    /**
     * 管理员通过学生账号查询学生的信息
     * @param student_num
     * @return
     */
    t_Student getStudentInfoById(String student_num);

    /**
     * 管理员查询学生信息返回list ，用的这个上面不用
     */
    List<t_Student> getStudentInfoByNum(String student_num);



    /**
     * 管理员通过输入的学生姓名查询信息
     */

    List<t_Student> getStudentInfoByName(String studentName);

    /**
     * 录入寝室信息
     * @param t_hotel
     */
    void insertHotel(t_Hotel t_hotel);

    /**
     * 通过输入的寝室栋进行查询寝室的信息
     */
    List<t_Hotel> selectHotelInfoByHotel(t_Hotel hotel);

    /**
     * 通过输入的寝室栋和寝室号进行查询寝室的信息
     */
    List<t_Hotel> selectHotelInfo(t_Hotel hotel);

    /**
     * 根据入住的学生然后更新寝室人数的信息
     */
    void updateRoomInfo(t_Hotel t_hotel);

    /**
     * 录入学生信息
     */
    void insetStudentInfo(t_Student t_student);

    /**
     * 展示所有的学生的信息
     */
    List<t_Student> getStudentAllInfo();

    /**
     * 管理员管理学生信息：删除学生信息
     */
    void delStudentInfo(String student_number);

    /**
     * 管理员管理学生信息：更新学生信息
     */
    void uptStudentInfo(t_Student t_student);

    /**
     * 管理员查询指定寝室的报修情况
     */
    List<t_Repair> getRepairInfo(t_Repair t_repair);
//    t_Repair getRepairInfo(t_Repair t_repair);

    /**
     * 管理员查询所有报修的寝室信息
     */
    List<t_Repair> getRepairAllInfo();

    /**
     * 管理员查询未完成的报修信息
     */
    List<t_Repair> getNoRepairInfo();

    /**
     * 管理员点击完成寝室维修任务
     */
    void succRepairInfo(t_Repair t_repair);

    /**
     *管理员查询全部宿舍信息
     */
    List<t_Hotel> hotelAllInfo();

    /**
     * 管理员删除指定寝室信息
     * @return
     */
    void delhotelInfo(t_Hotel t_hotel);

    /**
     * 超级管理员管理管理员
     */
    void insertAdmin(t_Admin t_admin);

    void delAdmin(t_Admin t_admin);

    void uptAdmin(t_Admin t_admin);


}
