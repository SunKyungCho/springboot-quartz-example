package com.example.schedulesample.dto;


import lombok.Getter;

@Getter
public class ScheduleRequest {

    private String jobGroup;
    private String scheduleName;
    private String cronExpression;

}
