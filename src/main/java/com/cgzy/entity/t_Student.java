package com.cgzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class t_Student implements Serializable {
    private String student_num;
    private String student_name;
    private String student_pw;
    private String sex;
    private String hotel_number;
    private String room_number;
    private String bed_number;
    private int statu;
}
