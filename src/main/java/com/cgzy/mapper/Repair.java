package com.cgzy.mapper;

import com.cgzy.entity.t_Repair;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Repair {
    /**
     * 通过寝室号查询寝室的报修信息
     */
    List<t_Repair> getRepairInfo(t_Repair t_repair);
//    t_Repair getRepairInfo(t_Repair t_repair);

    /**
     * 查询所有的报修，就是表上全部数据
     */
    List<t_Repair> getRepairAllInfo();

    /**
     * 查询未解决的报修信息
     */
    List<t_Repair> getNoRepairInfo();

    /**
     * 管理员点击完成寝室维修任务
     */

    void succRepairInfo(t_Repair t_repair);

    /**
     * 学生插入报修信息
     *
     */
    void insertRepairInfo(t_Repair t_repair);



}
