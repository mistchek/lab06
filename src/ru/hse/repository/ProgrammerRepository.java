package ru.hse.repository;

import ru.hse.model.Programmer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgrammerRepository
        implements Repository<Programmer, Long> {

    private Map<Long, Programmer> programmers =
            new HashMap<>();

    @Override
    public void save(Programmer entity) {
        programmers.put(entity.getId(), entity);
    }

    @Override
    public Programmer findById(Long id) {
        return programmers.get(id);
    }

    @Override
    public void delete(Long id) {
        programmers.remove(id);
    }

    @Override
    public List<Programmer> findAll() {
        return new ArrayList<>(programmers.values());
    }
}