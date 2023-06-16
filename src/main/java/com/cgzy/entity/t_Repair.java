package com.cgzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class t_Repair {
    private String hotel_number;
    private String room_number;
    private String repair;
    private String statu;
}
