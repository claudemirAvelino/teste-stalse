package com.stalse.inbox.infrastructure.web;

import com.stalse.inbox.application.metrics.ComputeMetricsUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final ComputeMetricsUseCase computeMetrics;

    public MetricsController(ComputeMetricsUseCase computeMetrics) {
        this.computeMetrics = computeMetrics;
    }

    @GetMapping
    public MetricsResponse get(@RequestParam(defaultValue = "7") int days) {
        return MetricsResponse.from(computeMetrics.execute(days));
    }
}
