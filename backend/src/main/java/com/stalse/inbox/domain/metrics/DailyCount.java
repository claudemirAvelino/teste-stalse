package com.stalse.inbox.domain.metrics;

import java.time.LocalDate;

public record DailyCount(LocalDate date, long count) {}
