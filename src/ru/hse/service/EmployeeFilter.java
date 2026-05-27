package ru.hse.service;

import ru.hse.model.Employee;

@FunctionalInterface
public interface EmployeeFilter {

    boolean test(Employee employee);
}