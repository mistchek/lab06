package ru.hse.service;

import ru.hse.model.Employee;
import ru.hse.model.Programmer;
import ru.hse.model.Grade;
import ru.hse.model.Task;
import ru.hse.model.Manager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.List;
import ru.hse.model.State;
import ru.hse.repository.FileRepository;
import ru.hse.exception.DataSaveException;
import ru.hse.exception.DataLoadException;
import ru.hse.security.AuthenticationService;
import ru.hse.security.Permission;
import ru.hse.util.Logger;
import ru.hse.exception.EmployeeNotFoundException;
import ru.hse.exception.TaskNotFoundException;
import ru.hse.exception.InvalidDataException;
import ru.hse.repository.CollectionRepository;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.function.Consumer;

public class HRMService {

    private CollectionRepository repository;
    private AuthenticationService authenticationService;

    public HRMService(CollectionRepository repository,
                      AuthenticationService authenticationService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
    }

    public CollectionRepository getRepository() {
        return repository;
    }

    public void setRepository(CollectionRepository repository) {
        this.repository = repository;
    }

    public ServiceResult<Employee> addEmployee(Employee employee) {

        if (employee == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Employee is null"
            );
        }

        if (!authenticationService.hasPermission(Permission.EDIT_EMPLOYEES)) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Access denied"
            );
        }

        repository.saveEmployee(employee);

        Logger.info("Employee added");

        return new ServiceResult<>(
                true,
                employee,
                null
        );
    }

    public ServiceResult<Employee> removeEmployeeById(Long id) {

        Employee employee = repository.findEmployeeById(id);

        if (employee == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Employee not found"
            );
        }

        repository.deleteEmployee(id);

        return new ServiceResult<>(
                true,
                employee,
                null
        );
    }

    public ServiceResult<Employee> updateSalary(Long id, BigDecimal newSalary) {

        if (newSalary == null || newSalary.compareTo(BigDecimal.ZERO) < 0) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Invalid salary"
            );
        }

        Employee employee = repository.findEmployeeById(id);

        if (employee == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Employee not found"
            );
        }

        employee.setSalary(newSalary);
        repository.saveEmployee(employee);

        Logger.info("Salary updated");

        return new ServiceResult<>(
                true,
                employee,
                null
        );
    }

    public ServiceResult<Employee> updateData(Long id, BigDecimal newSalary) {

        if (newSalary == null || newSalary.compareTo(BigDecimal.ZERO) < 0) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Invalid salary"
            );
        }

        Employee employee = repository.findEmployeeById(id);

        if (employee == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Employee not found"
            );
        }

        employee.setSalary(newSalary);
        repository.saveEmployee(employee);

        Logger.info("Employee data updated");

        return new ServiceResult<>(
                true,
                employee,
                null
        );
    }

    public ServiceResult<Double>
    getAverageSalaryByPosition(String position) {

        double averageSalary =
                this.repository.findAllEmployees()
                        .stream()
                        .filter(employee ->
                                employee.getPosition()
                                        .equalsIgnoreCase(position)
                        )
                        .map(Employee::getSalary)
                        .mapToDouble(BigDecimal::doubleValue)
                        .average()
                        .orElse(0);

        if (averageSalary == 0) {

            return new ServiceResult<>(
                    false,
                    null,
                    "Employees not found."
            );
        }

        return new ServiceResult<>(
                true,
                averageSalary,
                null
        );
    }

    public ServiceResult<Employee> findEmployeeWithMaxSalary() {

        Employee employee = this.repository.findAllEmployees()
                .stream()
                .max((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                .orElse(null);

        if (employee == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Employees not found"
            );
        }

        return new ServiceResult<>(
                true,
                employee,
                null
        );
    }

    public ServiceResult<Map<String, Long>>
    getEmployeeCountByPosition() {

        Map<String, Long> counts =
                this.repository.findAllEmployees()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Employee::getPosition,
                                Collectors.counting()
                        ));

        if (counts.isEmpty()) {

            return new ServiceResult<>(
                    false,
                    null,
                    "Employees not found."
            );
        }

        return new ServiceResult<>(
                true,
                counts,
                null
        );
    }

    public ServiceResult<Task> assignTaskToProgrammer(
            Long programmerId,
            Task task
    ) {

        Employee employee =
                repository.findEmployeeById(programmerId);

        if (!(employee instanceof Programmer)) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Programmer not found"
            );
        }

        if (task == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Task is null"
            );
        }

        repository.saveTask(task);
        repository.assignTaskToProgrammer(
                programmerId,
                task.getId()
        );

        Logger.info("Task assigned");

        return new ServiceResult<>(
                true,
                task,
                null
        );
    }

    public ServiceResult<Task> completeTask(Long taskId) {

        Task task =
                repository.findTaskById(taskId);

        if (task == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Task not found"
            );
        }

        task.setState(State.DONE);

        Logger.info("Task completed");

        return new ServiceResult<>(
                true,
                task,
                null
        );
    }

    public void saveAllData() {

        FileRepository repository = new FileRepository();

        try {
            repository.saveEmployees(this.repository.findAllEmployees());
            repository.saveTasks(this.repository.findAllTasks());
            repository.saveProgrammerTasks(this.repository.getProgrammerTasks());
            Logger.info("Data saved successfully");

        } catch (DataSaveException e) {
            System.out.println("Ошибка сохранения данных");
            Logger.error("Error while saving data");
        }
    }

    public void loadAllData() {

        FileRepository repository = new FileRepository();

        try {
            for (Employee employee : repository.loadEmployees()) {
                this.repository.saveEmployee(employee);
            }

            for (Task task : repository.loadTasks()) {
                this.repository.saveTask(task);
            }
            this.repository.setProgrammerTasks(repository.loadProgrammerTasks());

            Logger.info("Data loaded successfully");

        } catch (DataLoadException e) {
            System.out.println("Ошибка загрузки данных");
            Logger.error("Error while loading data");
        }
    }
    public ServiceResult<List<Employee>> printAllEmployees() {

        List<Employee> employees =
                this.repository.findAllEmployees();

        if (employees.isEmpty()) {

            return new ServiceResult<>(
                    false,
                    null,
                    "Employees not found"
            );
        }

        return new ServiceResult<>(
                true,
                employees,
                null
        );
    }

    public Map<Grade, List<Programmer>>
    getProgrammersGroupedByGrade() {

        return repository.findAllEmployees()
                .stream()
                .filter(employee ->
                        employee instanceof Programmer)
                .map(employee ->
                        (Programmer) employee)
                .collect(Collectors.groupingBy(
                        Programmer::getGrade
                ));
    }

    public ServiceResult<List<Programmer>> printProgrammersByGrade(Grade grade) {

        List<Programmer> programmers =
                new ArrayList<>();

        for (Employee employee :
                this.repository.findAllEmployees()) {

            if (employee instanceof Programmer) {

                Programmer programmer =
                        (Programmer) employee;

                if (programmer.getGrade() == grade) {
                    programmers.add(programmer);
                }
            }
        }

        if (programmers.isEmpty()) {

            return new ServiceResult<>(
                    false,
                    null,
                    "Programmers not found"
            );
        }

        return new ServiceResult<>(
                true,
                programmers,
                null
        );
    }

    public ServiceResult<List<Task>>
    getTasksByEmployeeId(Long employeeId) {

        List<Task> tasks =
                this.repository.getTasksByProgrammer(employeeId);

        if (tasks == null || tasks.isEmpty()) {

            return new ServiceResult<>(
                    false,
                    null,
                    "Сотрудник не найден или задач нет."
            );
        }

        return new ServiceResult<>(
                true,
                tasks,
                null
        );
    }

    public ServiceResult<Programmer> getProgrammerByTaskIdAndManagerId(
            Long taskId,
            Long managerId
    ) {

        Programmer programmer =
                repository.getProgrammerByTaskIdAndManagerId(taskId, managerId);

        if (programmer == null) {
            return new ServiceResult<>(
                    false,
                    null,
                    "Программист не найден."
            );
        }

        return new ServiceResult<>(
                true,
                programmer,
                null
        );
    }

    public ServiceResult<List<Employee>>
    printEmployeesWithExperienceMoreThan(int n) {

        List<Employee> employees =
                new ArrayList<>();

        for (Employee employee :
                repository.findAllEmployees()) {

            long years =
                    ChronoUnit.YEARS.between(
                            employee.getHireDate(),
                            LocalDate.now()
                    );

            if (years > n) {
                employees.add(employee);
            }
        }

        if (employees.isEmpty()) {

            return new ServiceResult<>(
                    false,
                    null,
                    "Employees not found"
            );
        }

        return new ServiceResult<>(
                true,
                employees,
                null
        );
    }

    public List<Employee> filterEmployees(
            EmployeeFilter filter
    ) {

        List<Employee> result = new ArrayList<>();

        for (Employee employee :
                repository.findAllEmployees()) {

            if (filter.test(employee)) {
                result.add(employee);
            }
        }

        return result;
    }

    public List<Task> filterTasks(
            TaskFilter filter
    ) {

        List<Task> result = new ArrayList<>();

        for (Task task :
                repository.findAllTasks()) {

            if (filter.test(task)) {
                result.add(task);
            }
        }

        return result;
    }

    public void processEmployees(
            List<Employee> employees,
            Consumer<Employee> action
    ) {

        for (Employee employee : employees) {
            action.accept(employee);
        }
    }

    public <R> List<R> mapEmployees(
            List<Employee> employees,
            Function<Employee, R> mapper
    ) {

        List<R> result = new ArrayList<>();

        for (Employee employee : employees) {
            result.add(mapper.apply(employee));
        }

        return result;
    }

    public ServiceResult<List<Task>> printOverdueTasks() {

        List<Task> overdueTasks =
                repository.findAllTasks()
                        .stream()
                        .filter(task ->
                                task.getEndDay()
                                        .isBefore(LocalDate.now())
                                        &&
                                        task.getState()
                                                != State.DONE)
                        .collect(Collectors.toList());

        if (overdueTasks.isEmpty()) {

            return new ServiceResult<>(
                    false,
                    null,
                    "Overdue tasks not found"
            );
        }

        return new ServiceResult<>(
                true,
                overdueTasks,
                null
        );
    }
}
