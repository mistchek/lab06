package ru.hse.service;

import ru.hse.model.Task;

public interface TaskFilter {
    boolean test(Task task);
}