package com.mlv.cameldsl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Weather {

    static int counter = 1;
    private int id = counter++;
    private String city;
    private String unit;
    private String temp;
    private String time;
}
