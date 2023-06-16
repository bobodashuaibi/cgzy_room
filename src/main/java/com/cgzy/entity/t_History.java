package com.cgzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 历史表中的taskinst。展示历史流程的
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class t_History implements Serializable {
    private String ID_;
    private String PROC_INST_ID_;
    private String ASSIGNEE_;
}
