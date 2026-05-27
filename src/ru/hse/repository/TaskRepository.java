package ru.hse.repository;

import ru.hse.model.Task;
import java.util.List;

public interface TaskRepository {

    void saveTask(Task task);

    Task findTaskById(Long id);

    void deleteTask(Long id);

    List<Task> findAllTasks();
}