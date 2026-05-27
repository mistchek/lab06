package ru.hse.repository;

import ru.hse.model.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerRepository
        implements Repository<Manager, Long> {

    private Map<Long, Manager> managers =
            new HashMap<>();

    @Override
    public void save(Manager entity) {
        managers.put(entity.getId(), entity);
    }

    @Override
    public Manager findById(Long id) {
        return managers.get(id);
    }

    @Override
    public void delete(Long id) {
        managers.remove(id);
    }

    @Override
    public List<Manager> findAll() {
        return new ArrayList<>(managers.values());
    }
}