package com.example.schedulesample.controller;


import com.example.schedulesample.dto.ScheduleRequest;
import com.example.schedulesample.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final SchedulerService schedulerService;

    @PostMapping("/schedules")
    public void createSchedules(@RequestBody ScheduleRequest scheduleRequest) {
        schedulerService.addSchedule(scheduleRequest);
    }

}
