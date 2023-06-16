package com.cgzy.common.lang;

import cn.hutool.core.map.MapUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class result implements Serializable {

    private String token;
    private int code;
    private String msg;
    private Object data;

    //登录成功
    public static result Loginsucc(String token,Object data){
//        System.out.println("返回结果中的"+token);
        return succ(token,200,"登录成功",
                MapUtil.builder ()
                        .put ("list", data)
                        .map ());
    }

    //进入请假申请成功
    public static result startLeavesucc(String token,boolean b,Object data){
//        System.out.println("返回结果中的"+token);
        if (b==true){
            return succ(token,200,"申请成功",MapUtil.builder ()
                    .put ("list", data)
                    .map ());
        }else {
            return succ(token,400,"申请失败",MapUtil.builder ()
                    .put ("list", data)
                    .map ());
        }
    }

    //审核请假
    public static result passLeave(String token,int code,String msg){
//        System.out.println("返回结果中的"+token);
            return succ(token,code,msg,null);
    }


    //用户查询待办
    public static result selectdaiban(String token, List list){
//        System.out.println("返回结果中的"+token);
            return succ(token,200,"申请成功",list);
    }



    //成功
    public static result Logoutsucc(){
//        System.out.println("返回结果中的"+token);
        return succ(null,200,"退出成功",null);
    }





    //成功
    public static result Regsucc(Object data){
        return succ(200,"注册成功",data);
    }


    public static result succ(String token,int code,String msg,Object data){
        result rs = new result();
        rs.setToken(token);
        rs.setCode(code);
        rs.setMsg(msg);
        rs.setData(data);
        return rs;
    }
    public static result succ(int code,String msg,Object data){
        result rs = new result();
        rs.setCode(code);
        rs.setMsg(msg);
        rs.setData(MapUtil.builder ()
                .put ("list", data)
                .map ());
        return rs;
    }
    //失败
    public static result Loginfail(String msg){
        return fail(400,msg,null);
    }


    public static result fail(int code,String msg,Object data){
        result rs = new result();
        rs.setCode(400);
        rs.setMsg(msg);
        rs.setData(data);
        return rs;
    }


}
