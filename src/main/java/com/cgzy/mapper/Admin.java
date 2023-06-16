package com.cgzy.mapper;

import com.cgzy.entity.t_Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Admin {

    t_Admin getAdminInfoById(String admin_num);

    List<t_Admin> getAdminInfo();

    List<t_Admin> getAdminById(String admin_num);

    /**
     * 超级管理员管理管理员
     */
    void insertAdmin(t_Admin t_admin);

    void delAdmin(t_Admin t_admin);

    void uptAdmin(t_Admin t_admin);

}
