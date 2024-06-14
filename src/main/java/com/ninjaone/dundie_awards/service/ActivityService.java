package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.controller.dto.ActivityDto;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Transactional
    public Activity saveNewActivity(ActivityDto activityDto) {
        var activity = new Activity(activityDto.getOccuredAt(), activityDto.getEvent());
        return activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public List<Activity> getNonProcessedActivities() {
        return activityRepository.findAllByProcessed(false);
    }

    @Transactional
    public void setActivityProcessed(Long activityId) {
        var activity = activityRepository.getReferenceById(activityId);
        activity.setProcessed(true);
        activityRepository.save(activity);
    }
}
