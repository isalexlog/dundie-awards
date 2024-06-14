package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.controller.dto.ActivityDto;
import com.ninjaone.dundie_awards.service.ActivityProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityProcessor activityProcessor;

    public ActivityController(ActivityProcessor activityProcessor) {
        this.activityProcessor = activityProcessor;
    }

    @PostMapping
    public ResponseEntity<?> createActivity(@RequestBody ActivityDto activityDto) {
        try {
            activityProcessor.processActivity(activityDto);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
