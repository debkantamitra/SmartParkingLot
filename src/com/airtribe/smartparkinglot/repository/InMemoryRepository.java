package com.airtribe.smartparkinglot.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<T> implements Repository<T> {
    private final Map<String, T> values = new LinkedHashMap<>();

    @Override
    public void add(String id, T value) {
        if (values.containsKey(id)) {
            throw new IllegalStateException("Record already exists: " + id);
        }
        values.put(id, value);
    }

    @Override
    public void update(String id, T value) {
        if (!values.containsKey(id)) {
            throw new IllegalStateException("Record does not exist: " + id);
        }
        values.put(id, value);
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
