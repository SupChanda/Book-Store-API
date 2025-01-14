package com.book.store.service.impl;

import com.book.store.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.SystemHealth;
import org.springframework.stereotype.Service;

@Service
public class MonitoringServiceImpl implements MonitoringService {
    @Autowired
    private HealthEndpoint healthEndpoint;
    @Override
    public boolean isDatabaseUp() {
        SystemHealth health = (SystemHealth) healthEndpoint.health();
        System.out.println(health.getComponents().get("db"));
        return health.getComponents().get("db").getStatus().getCode().equals(Status.UP.getCode());
    }
}
