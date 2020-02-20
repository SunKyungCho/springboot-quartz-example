package com.example.schedulesample.service;

import com.example.schedulesample.domain.Index;
import com.example.schedulesample.dto.ScheduleRequest;
import com.example.schedulesample.job.IndexJob;
import com.example.schedulesample.repository.IndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.ParseException;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final ApplicationContext context;

    public void addSchedule(ScheduleRequest scheduleRequest) {

        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("time", new Date());

            JobDetailFactoryBean jobFactoryBean = new JobDetailFactoryBean();
            jobFactoryBean.setJobClass(IndexJob.class);
            jobFactoryBean.setDurability(false);
            jobFactoryBean.setApplicationContext(context);
            jobFactoryBean.setName(scheduleRequest.getScheduleName());
            jobFactoryBean.setGroup(scheduleRequest.getJobGroup());
            jobFactoryBean.setJobDataMap(jobDataMap);
            jobFactoryBean.afterPropertiesSet();

            CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
            triggerFactoryBean.setName(scheduleRequest.getScheduleName());
            triggerFactoryBean.setGroup(scheduleRequest.getJobGroup());
            triggerFactoryBean.setCronExpression(scheduleRequest.getCronExpression());
            triggerFactoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW); //바로 실행
            triggerFactoryBean.afterPropertiesSet();

            JobKey jobKey = new JobKey(scheduleRequest.getScheduleName(), scheduleRequest.getJobGroup());

            Date date = schedulerFactoryBean.getScheduler().scheduleJob(jobFactoryBean.getObject(), triggerFactoryBean.getObject());

            log.debug("Job with jobKey : {} scheduled successfully at date : {}", jobFactoryBean.getObject().getKey(), date);
        } catch (ObjectAlreadyExistsException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (SchedulerException | ParseException e) {
            e.printStackTrace();
        }
    }


}
