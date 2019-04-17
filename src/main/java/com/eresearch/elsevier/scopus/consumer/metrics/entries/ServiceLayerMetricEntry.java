package com.eresearch.elsevier.scopus.consumer.metrics.entries;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.eresearch.elsevier.scopus.consumer.service.ElsevierScopusConsumerService;

@Component
public class ServiceLayerMetricEntry {

    @Qualifier("appMetricRegistry")
    @Autowired
    private MetricRegistry metricRegistry;

    private Timer serviceLayerTimer;

    @PostConstruct
    public void init() {
        registerTimers();
    }

    private void registerTimers() {

        String timerName = MetricRegistry.name(ElsevierScopusConsumerService.class,
                "elsevierScopusConsumerOperation",
                "timer");

        serviceLayerTimer = metricRegistry.timer(timerName);
    }

    public Timer getServiceLayerTimer() {
        return serviceLayerTimer;
    }
}
