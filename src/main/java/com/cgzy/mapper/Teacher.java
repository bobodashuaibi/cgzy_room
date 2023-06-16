package com.cgzy.mapper;


import com.cgzy.entity.t_Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface Teacher {

    /**
     *     查询老师用户
     */
    t_Teacher selectTeacherInfo(String teacher_num);



}
