package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private MessageBroker messageBroker;

    @MockBean
    private AwardsCache awardsCache;

    @Test
    public void getIndexShouldReturnIndexViewWithModelAttributes() throws Exception {
        List<Employee> employees = new ArrayList<>();

        var organization = new Organization();
        var employee = new Employee();
        employee.setOrganization(organization);
        employees.add(employee);

        BlockingQueue<Activity> messages = new LinkedBlockingQueue<>();
        var activity = new Activity();
        messages.add(activity);

        List<Activity> activities = new ArrayList<>();
        activities.add(activity);

        int totalDundieAwards = 5;

        when(employeeRepository.findAll()).thenReturn(employees);
        when(activityRepository.findAll()).thenReturn(activities);
        when(messageBroker.getMessages()).thenReturn(messages);
        when(awardsCache.getTotalAwards()).thenReturn(totalDundieAwards);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("employees", employees))
                .andExpect(model().attribute("activities", activities))
                .andExpect(model().attribute("queueMessages", messages))
                .andExpect(model().attribute("totalDundieAwards", totalDundieAwards));

        verify(employeeRepository, atLeastOnce()).findAll();
        verify(activityRepository).findAll();
        verify(messageBroker).getMessages();
        verify(awardsCache).getTotalAwards();
    }
}
