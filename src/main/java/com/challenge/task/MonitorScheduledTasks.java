package com.challenge.task;

import com.challenge.domain.HealthStatus;
import com.challenge.magnificent.MagnificentClient;
import com.challenge.magnificent.MagnificentMetrics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorScheduledTasks {

    private MagnificentClient magnificentClient;

    private MagnificentMetrics magnificentMetrics;

    public MonitorScheduledTasks(MagnificentClient magnificentClient, MagnificentMetrics magnificentMetrics) {
        this.magnificentClient = magnificentClient;
        this.magnificentMetrics = magnificentMetrics;
    }

    @Scheduled(fixedRateString = "${monitor.magnificent.status.delay}")
    public void checkHealthMonitor() {
        HealthStatus status = magnificentClient.checkHealth();
        magnificentMetrics.addStatus(status);
    }

    @Scheduled(fixedRateString = "${monitor.magnificent.metrics.delay}")
    public void canculateMinutesMetrics() {
        magnificentMetrics.calculateMetrics();
    }
}
