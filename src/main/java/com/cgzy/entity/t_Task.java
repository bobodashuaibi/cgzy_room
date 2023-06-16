package com.cgzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class t_Task implements Serializable {
    private String ID_;
    private String PROC_INST_ID_;
    private String BUSINESS_KEY_;
}
