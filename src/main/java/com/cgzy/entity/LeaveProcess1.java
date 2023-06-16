package com.cgzy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("请假表")
public class LeaveProcess1 implements Serializable{
	@ApiModelProperty("流程实例id")
	String process_instance_id;

	@ApiModelProperty("busKey")
	String business_key;


	@ApiModelProperty("用户账号")
	String user_id;

	@ApiModelProperty("用户名")
	String user_name;

	@ApiModelProperty("请假起始时间")
	String start_time;
	
	@ApiModelProperty("请假结束时间")
	String end_time;
	
	@ApiModelProperty("请假类型")
	String leave_type;
	
	@ApiModelProperty("请假原因")
	String reason;
	
	@ApiModelProperty("申请时间")
	String apply_time;

//	0未开始审核，1审核，2完成
	@ApiModelProperty("当前状态")
	String state;

}
