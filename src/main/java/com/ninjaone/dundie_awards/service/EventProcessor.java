package com.ninjaone.dundie_awards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.controller.dto.EventDto;
import com.ninjaone.dundie_awards.model.Activity;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
public class EventProcessor {

    private final static Logger log = LoggerFactory.getLogger(EventProcessor.class);

    private final MessageBroker messageBroker;
    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;
    private final ExecutorService threadPoolExecutor;

    private volatile boolean running = true;


    public EventProcessor(MessageBroker messageBroker,
                          EmployeeService employeeService,
                          ObjectMapper objectMapper) {
        this.messageBroker = messageBroker;
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
        this.threadPoolExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @PostConstruct
    public void start() {
        Runnable consumerTask = () -> {
            while (running) {
                Activity activity;
                try {
                    activity = messageBroker.receiveMessage();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                try {
                    processEvent(readMessage(activity.getEvent()), activity.getId());
                } catch (Exception e) {
                    try {
                        messageBroker.sendMessage(activity);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(
                                "Activity " + activity.toString() + " was not processed and failed to put back into queue for processing",
                                ex
                        );
                    }
                }
            }
        };

        IntStream.range(0, 5).forEach(i -> threadPoolExecutor.submit(consumerTask));
    }

    @PreDestroy
    public void stop() {
        running = false;
        threadPoolExecutor.shutdownNow();
        try {
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                log.error("Thread pool did not terminate");
            }
        } catch (InterruptedException e) {
            threadPoolExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void processEvent(EventDto eventDto, long activityId) {
        employeeService.addAwardToEmployee(eventDto.getEmployeeId(), activityId);
    }

    public EventDto readMessage(String event) {
        try {
            return objectMapper.readValue(event, EventDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't parse activity event", e);
        }
    }
}
