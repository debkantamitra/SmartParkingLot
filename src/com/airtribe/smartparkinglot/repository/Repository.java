package com.airtribe.smartparkinglot.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void add(String id, T value);

    void update(String id, T value);

    Optional<T> findById(String id);

    List<T> findAll();

    Optional<T> remove(String id);
}
