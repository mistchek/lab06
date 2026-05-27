package ru.hse.service;

import ru.hse.model.Task;

@FunctionalInterface
public interface TaskFilter {

    boolean test(Task task);
}