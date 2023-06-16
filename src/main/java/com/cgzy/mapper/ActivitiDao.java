package com.cgzy.mapper;


import com.cgzy.entity.LeaveProcess;
import com.cgzy.entity.t_History;
import com.cgzy.entity.t_Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivitiDao {
    /**
     *通过businessKey查询业务表leaveinfo
     */
    LeaveProcess getLeaveInfo(String processInstanceId);
    /**
     *进去查询该用户的所有历史和现在活动的流程信息
     */
    List<LeaveProcess> selectInfo();

    /**
     * 插入一条请假的业务信息
     * @param leaveProcess
     */
    void insertLeaveInfo(LeaveProcess leaveProcess);


    t_Task getTaskIdByProcess(String business_key);

    /**
     * 历史信息，传入当前的登录的用户名 在taskinst进行查询processId
     */
    List<t_History> getHistoryInfoByUserName(String username);

    /**
     *通过ProcessId查询业务表leaveinfo
     */
    LeaveProcess getLeaveInfoByProcessId(String ProcessId);

    /**
     * 更改状态
     */
    void uptStatePass(String proId);
    void uptStateNoPass(String proId);

}
