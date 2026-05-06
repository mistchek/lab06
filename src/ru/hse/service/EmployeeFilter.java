package ru.hse.service;

import ru.hse.model.Employee;

public interface EmployeeFilter {
    boolean test(Employee employee);
}