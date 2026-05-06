package ru.hse.repository;

import ru.hse.model.Employee;
import ru.hse.model.Manager;
import ru.hse.model.Programmer;
import ru.hse.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionRepository {

    private Map<Long, Employee> employees = new HashMap<>();

    private Map<Long, Task> tasks = new HashMap<>();

    private Map<Long, List<Long>> programmerTasks = new HashMap<>();

    private Map<Long, List<Long>> managerProgrammers = new HashMap<>();


    public void saveEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
    }

    public Employee findEmployeeById(Long id) {
        return employees.get(id);
    }

    public void deleteEmployee(Long id) {
        employees.remove(id);
    }

    public List<Employee> findAllEmployees() {
        return new ArrayList<>(employees.values());
    }


    public List<Programmer> findAllProgrammers() {

        List<Programmer> programmers = new ArrayList<>();

        for (Employee employee : employees.values()) {

            if (employee instanceof Programmer programmer) {
                programmers.add(programmer);
            }
        }

        return programmers;
    }


    public List<Manager> findAllManagers() {

        List<Manager> managers = new ArrayList<>();

        for (Employee employee : employees.values()) {

            if (employee instanceof Manager manager) {
                managers.add(manager);
            }
        }

        return managers;
    }


    public void saveTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task findTaskById(Long id) {
        return tasks.get(id);
    }

    public void deleteTask(Long id) {
        tasks.remove(id);
    }

    public List<Task> findAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    public void assignTaskToProgrammer(Long programmerId, Long taskId) {

        programmerTasks.putIfAbsent(programmerId, new ArrayList<>());

        programmerTasks.get(programmerId).add(taskId);
    }


    public List<Task> getTasksByProgrammer(Long programmerId) {

        List<Task> result = new ArrayList<>();

        List<Long> taskIds = programmerTasks.get(programmerId);

        if (taskIds != null) {

            for (Long id : taskIds) {

                Task task = tasks.get(id);

                if (task != null) {
                    result.add(task);
                }
            }
        }

        return result;
    }


    public void assignProgrammerToManager(Long managerId, Long programmerId) {

        managerProgrammers.putIfAbsent(managerId, new ArrayList<>());

        managerProgrammers.get(managerId).add(programmerId);
    }
    public Map<Long, List<Long>> getProgrammerTasks() {
        return programmerTasks;
    }
    public void setProgrammerTasks(Map<Long, List<Long>> programmerTasks) {
        this.programmerTasks = programmerTasks;
    }

    public List<Programmer> getProgrammersByManager(Long managerId) {

        List<Programmer> result = new ArrayList<>();

        List<Long> programmerIds = managerProgrammers.get(managerId);

        if (programmerIds != null) {

            for (Long id : programmerIds) {

                Employee employee = employees.get(id);

                if (employee instanceof Programmer programmer) {
                    result.add(programmer);
                }
            }
        }

        return result;
    }
}