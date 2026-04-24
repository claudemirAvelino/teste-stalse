package com.stalse.inbox.domain.shared;

import java.util.List;

public record PagedResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements
) {
    public int totalPages() {
        if (size <= 0) return 0;
        return (int) Math.ceil((double) totalElements / size);
    }
}
