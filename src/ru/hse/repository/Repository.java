package ru.hse.repository;

import java.util.List;

public interface Repository<T, K> {

    void save(T entity);

    T findById(K id);

    void delete(K id);

    List<T> findAll();
}