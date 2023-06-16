package com.cgzy.utils;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.cgzy.entity.t_Task;

@Component
public class ActivitiUtil {
    /**
     * 唯一的请假流程部署的id
     */
    private static final String myLeaveId="myProcess";

    /**
     * 进行部署项目的bpmn和png
     * 流程部署
     *  影响的表
     *      act_re_procdef       流程定义表
     *      act_re_deployment    流程部署表
     *      act_ge_bytearray     通用的流程定义和流程资源
     *
     *      ge 代表  general     通用的
     *      re 代表  repository
     */
    public static  void initDeploymentBPMN(){
        String fileName= "bpmn/cgzy.bpmn";
        String imageName = "bpmn/cgzy.png";
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
                .addClasspathResource(imageName)
                .name("学生请假流程")
                .deploy();
    }
    /**
     * 开始定义一个实例，点击请假申请就进行定义实例
     * @param
     */
    public static  ProcessInstance startProcessAndAssigneeValue(HashMap map,String busKey) {
        //1. 通过工具类（ProcessEngines）获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2. 得到RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3. 设置assignee的取值，用户在界面上设置流程的执行人

        //4. 启动流程实例，同时还要设置流程定义的assignee的值
        //5. 这个id就是我们bpmn定义的整个流程的id
//        定义一个busKey进行叠加

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(myLeaveId,busKey,map);
//        返回当前开启实例的id
        return processInstance;
    }

    /**
     * 学生完成提交请假申请
     */
    public static void handleWithTaskByassignee(String username,String taskId) {
        //1. 通过工具类（ProcessEngines）获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 得到TaskService实例
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId);
    }


    /**
     * 通过辅导员完成当前的任务
     */
    public static void passTaskByfudaoyuan(String username,String task_id,String type,int time) {
        //1. 通过工具类（ProcessEngines）获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 得到TaskService实例
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariable(task_id,"fudaoyuanType",type);
        taskService.setVariable(task_id,"time",time);
        taskService.complete(task_id);
    }

    /**
     * 通过学生办公室完成当前的任务
     */
    public static void passTaskByOffice(String username,String task_id,String type) {
        //1. 通过工具类（ProcessEngines）获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2. 得到TaskService实例
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariable(task_id,"student_office_approve",type);
        taskService.complete(task_id);
    }
    /**
     * 查询指定任务人的待办任务
     */
    public static List<Task> getTaskByAssignee(String name){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(myLeaveId)
                .taskAssignee(name)
                .orderByTaskCreateTime().asc()
                .list();
        return list;
    }



    /**
     * 可以查询   当前用户未完成的任务,获取的businessId
     */
    public static List<HistoricTaskInstance> getTasks(String username){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricTaskInstance> list=engine.getHistoryService() // 历史任务Servic     这一行报null是什么情况？
                .createHistoricTaskInstanceQuery() // 创建历史任务实例查询
                .taskAssignee(username) // 指定办理人
                .unfinished()// 查询未完成的任务
                .list();
        return list;
    }

    /**
     * 可以查询   当前用户完成的任务,获取的businessId
     */
    public static List<HistoricTaskInstance> getNoTasks(String username){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricTaskInstance> list=engine.getHistoryService() // 历史任务Servic     这一行报null是什么情况？
                .createHistoricTaskInstanceQuery() // 创建历史任务实例查询
                .taskAssignee(username) // 指定办理人
                .finished()// 查询已经完成的任务
                .list();
        return list;
    }






}
