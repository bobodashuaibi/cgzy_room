package com.cgzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class t_Admin implements Serializable {
    private String admin_num;
    private String admin_name;
    private String admin_pw;
    private String admin_room;
}
