package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.controller.dto.ActivityDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ActivityProcessor {
    private static final Logger log = LoggerFactory.getLogger(ActivityProcessor.class);

    public final ActivityService activityService;
    public final MessageBroker messageBroker;

    public ActivityProcessor(ActivityService activityService, MessageBroker messageBroker) {
        this.activityService = activityService;
        this.messageBroker = messageBroker;
    }

    @Transactional
    public void processActivity(ActivityDto activityDto) {
        var activity = activityService.saveNewActivity(activityDto);
        try {
            messageBroker.sendMessage(activity);
        } catch (InterruptedException e) {
            log.error("Error publishing activity message", e);
            throw new RuntimeException(e);
        }
    }
}
