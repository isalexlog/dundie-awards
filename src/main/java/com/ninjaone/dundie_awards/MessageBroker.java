package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.service.ActivityService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageBroker {

    private final ActivityService activityService;

    private final BlockingQueue<Activity> messages = new LinkedBlockingQueue<>();

    public MessageBroker(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostConstruct
    public void init() throws InterruptedException {
        for (Activity activity : activityService.getNonProcessedActivities()) {
            sendMessage(activity);
        }
    }

    public void sendMessage(Activity message) throws InterruptedException {
        messages.put(message);
    }

    public Activity receiveMessage() throws InterruptedException {
        return messages.take();
    }

    public BlockingQueue<Activity> getMessages(){
        return messages;
    }
}
