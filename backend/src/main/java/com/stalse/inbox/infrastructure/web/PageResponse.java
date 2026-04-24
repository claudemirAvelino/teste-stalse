package com.stalse.inbox.infrastructure.web;

import com.stalse.inbox.domain.shared.PagedResult;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <D, R> PageResponse<R> map(PagedResult<D> source, Function<D, R> mapper) {
        return new PageResponse<>(
                source.content().stream().map(mapper).toList(),
                source.page(),
                source.size(),
                source.totalElements(),
                source.totalPages()
        );
    }
}
