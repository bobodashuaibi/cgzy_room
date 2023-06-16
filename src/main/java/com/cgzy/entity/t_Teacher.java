package com.cgzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class t_Teacher {
    private String teacher_num;
    private String teacher_name;
    private String password;
    private String job;
}
