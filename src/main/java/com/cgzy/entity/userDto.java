package com.cgzy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class userDto implements Serializable {
    private String mail_num;
    private String student_pw;
    private String mail;

}
