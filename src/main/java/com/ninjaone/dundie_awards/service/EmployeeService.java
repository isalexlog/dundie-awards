package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AwardsCache awardsCache;
    private final ActivityService activityService;

    public EmployeeService(EmployeeRepository employeeRepository, AwardsCache awardsCache, ActivityService activityService) {
        this.employeeRepository = employeeRepository;
        this.awardsCache = awardsCache;
        this.activityService = activityService;
    }

    @Transactional
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow();
    }


    @Transactional
    public void addAwardToEmployee(Long id, long activityId) {
        var employee = getEmployee(id);
        employee.setDundieAwards(employee.getDundieAwards() + 1);
        employeeRepository.save(employee);
        activityService.setActivityProcessed(activityId);
        awardsCache.addOneAward();
    }
}
