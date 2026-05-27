package ru.hse.repository;

import ru.hse.model.Employee;
import java.util.List;

public interface EmployeeRepository {

    void saveEmployee(Employee employee);

    Employee findEmployeeById(Long id);

    void deleteEmployee(Long id);

    List<Employee> findAllEmployees();
}