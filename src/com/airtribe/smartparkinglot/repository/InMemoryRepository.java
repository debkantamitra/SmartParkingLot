package com.airtribe.smartparkinglot.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryRepository<T> implements Repository<T> {
    private final ConcurrentMap<String, T> values = new ConcurrentHashMap<>();

    @Override
    public void add(String id, T value) {
        if (values.putIfAbsent(id, value) != null) {
            throw new IllegalStateException("Record already exists: " + id);
        }
    }

    @Override
    public void update(String id, T value) {
        if (values.replace(id, value) == null) {
            throw new IllegalStateException("Record does not exist: " + id);
        }
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(values.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(values.values());
    }

    @Override
    public Optional<T> remove(String id) {
        return Optional.ofNullable(values.remove(id));
    }
}
