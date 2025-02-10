package com.book.store.Controller;

import com.book.store.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    private final MonitoringService monitoringService;

    @Autowired
    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping(value = "/monitoring/health")
    public ResponseEntity<String> healthCheck() {
        if (!monitoringService.isDatabaseUp()) {
            return new ResponseEntity<>("db is down", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("server is up", HttpStatus.OK);
    }
}
