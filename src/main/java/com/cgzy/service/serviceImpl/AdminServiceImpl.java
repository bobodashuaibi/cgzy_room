package com.cgzy.service.serviceImpl;

import com.cgzy.entity.t_Admin;
import com.cgzy.entity.t_Hotel;
import com.cgzy.entity.t_Repair;
import com.cgzy.entity.t_Student;
import com.cgzy.mapper.Admin;
import com.cgzy.mapper.Hotel;
import com.cgzy.mapper.Repair;
import com.cgzy.mapper.Student;
import com.cgzy.service.AdminService;
import com.cgzy.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private Admin adminDao;
    @Resource
    private Student studentDao;
    @Resource
    private Hotel hotelDao;
    @Resource
    private Repair repairDao;

    /**
     * 根据账号查找管理员用户
     * @param
     * @return
     */
    @Override
    public t_Admin getAdminInfoById(String admin_num) {
        return adminDao.getAdminInfoById(admin_num);
    }

    @Override
    public List<t_Admin> getAdminById(String admin_num) {
        return adminDao.getAdminById(admin_num);
    }

    @Override
    public List<t_Admin> getAdminInfo() {
        return adminDao.getAdminInfo();
    }

    /**
     * 管理员通过学生账号查询学生的信息
     * @param student_num
     * @return
     */
    @Override
    public t_Student getStudentInfoById(String student_num) {
        return studentDao.getStudentInfoById(student_num);
    }

    @Override
    public List<t_Student> getStudentInfoByNum(String student_num) {
        return studentDao.getStudentInfoByNum(student_num);
    }

    /**
     * 管理员通过学生姓名查询学生的信息
     * @param studentName
     * @return
     */
    @Override
    public List<t_Student> getStudentInfoByName(String studentName) {
        return studentDao.getStudentInfoByName(studentName);
    }

    /**
     * 录入寝室信息
     * @param t_hotel
     */
    @Override
    public void insertHotel(t_Hotel t_hotel) {
//        return hotel.insertHotel(t_hotel);
        hotelDao.insertHotel(t_hotel);
    }

    /**
     * 通过输入的寝室栋进行查询寝室的信息
     */
    @Override
    public List<t_Hotel> selectHotelInfoByHotel(t_Hotel t_hotel) {
        List<t_Hotel> t_hotels = hotelDao.selectHotelInfoByHotel(t_hotel);
        return t_hotels;
    }

    /**
     * 通过输入的寝室栋和寝室号去进行查询寝室的信息
     */
    @Override
    public List<t_Hotel> selectHotelInfo(t_Hotel t_hotel) {
        List<t_Hotel> t_hotels = hotelDao.selectHotelInfo(t_hotel);
        return t_hotels;
    }

    /**
     * 更新寝室里面入住的人数
     */
    @Override
    public void updateRoomInfo(t_Hotel t_hotel) {
//        1、首先进行查询学生的寝室，同一个
//        获取到传过来的寝室号的有几个人
        int people_num = studentDao.selectRoomByPeople(t_hotel);
        String hotel_number = t_hotel.getHotel_number();
        String room_number = t_hotel.getRoom_number();
        hotelDao.updateRoomPeople(people_num,hotel_number,room_number);
    }

    /**
     * 录入学生信息
     * @param t_student
     */
    @Override
    public void insetStudentInfo(t_Student t_student) {
        studentDao.insertStudentInfo(t_student);
    }

    /**
     * 查询所有学生的信息
     * @return
     */
    @Override
    public List<t_Student> getStudentAllInfo() {
        List<t_Student> studentAllInfo = studentDao.getStudentAllInfo();
        return studentAllInfo;
    }
    /**
     * 管理员管理学生信息：删除学生信息
     */
    @Override
    public void delStudentInfo(String student_number) {
        studentDao.delStudentInfo(student_number);
    }
    /**
     * 管理员管理学生信息：更新学生信息
     */
    @Override
    public void uptStudentInfo(t_Student t_student) {
        studentDao.uptStudentInfo(t_student);
    }
    /**
     * 管理员查询指定寝室的报修情况
     * @param t_repair
     * @return
     */
    @Override
    public List<t_Repair> getRepairInfo(t_Repair t_repair) {
        return repairDao.getRepairInfo(t_repair);
    }

    /**
     * 查询所有的报修情况
     * @return
     */
    @Override
    public List<t_Repair> getRepairAllInfo() {
        return repairDao.getRepairAllInfo();
    }

    /**
     * 管理员查询未完成的报修信息
     */
    @Override
    public List<t_Repair> getNoRepairInfo() {
        return repairDao.getNoRepairInfo();
    }
    /**
     * 管理员点击完成寝室维修任务
     */
    @Override
    public void succRepairInfo(t_Repair t_repair) {
        repairDao.succRepairInfo(t_repair);
    }

    @Override
    public List<t_Hotel> hotelAllInfo() {
        return hotelDao.hotelAllInfo();
    }

    @Override
    public void delhotelInfo(t_Hotel t_hotel) {
        hotelDao.delhotelInfo(t_hotel);
    }

    @Override
    public void insertAdmin(t_Admin t_admin) {
        adminDao.insertAdmin(t_admin);
    }

    @Override
    public void delAdmin(t_Admin t_admin) {
        adminDao.delAdmin(t_admin);
    }

    @Override
    public void uptAdmin(t_Admin t_admin) {
        adminDao.uptAdmin(t_admin);
    }

}
