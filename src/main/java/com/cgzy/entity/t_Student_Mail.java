package com.cgzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class t_Student_Mail implements Serializable {
    private String student_num;
    private String mail_num;
}
