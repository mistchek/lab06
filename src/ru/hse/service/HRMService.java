package ru.hse.service;

import ru.hse.model.Employee;
import ru.hse.model.Programmer;
import ru.hse.model.Grade;
import ru.hse.model.Task;
import ru.hse.model.Manager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import ru.hse.model.State;
import ru.hse.repository.FileRepository;
import ru.hse.exception.DataSaveException;
import ru.hse.exception.DataLoadException;
import ru.hse.util.Logger;
import ru.hse.exception.EmployeeNotFoundException;
import ru.hse.exception.TaskNotFoundException;
import ru.hse.exception.InvalidDataException;

public class HRMService {
    private Employee[] employees;
    private Task[] tasks;

    public HRMService(Employee[] employees, Task[] tasks) {
        this.employees = employees;
        this.tasks = tasks;
    }

    public HRMService(Employee[] employees) {
        this.employees = employees;
        this.tasks = new Task[0];
    }

    public Employee[] getEmployees() {
        return employees;
    }

    public void setEmployees(Employee[] employees) {
        this.employees = employees;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }

    public void addEmployee(Employee employee) {

        if (employee == null) {
            return;
        }

        Employee[] newEmployees = new Employee[employees.length + 1];

        for (int i = 0; i < employees.length; i++) {
            newEmployees[i] = employees[i];
        }

        newEmployees[employees.length] = employee;

        employees = newEmployees;
        Logger.info("Employee added");
    }

    public void removeEmployeeById(Long id) throws EmployeeNotFoundException {

        int index = -1;

        for (int i = 0; i < employees.length; i++) {
            if (employees[i].getId().equals(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        Employee[] newEmployees = new Employee[employees.length - 1];

        int j = 0;

        for (int i = 0; i < employees.length; i++) {
            if (i != index) {
                newEmployees[j] = employees[i];
                j++;
            }
        }

        employees = newEmployees;
        Logger.info("Employee removed");
    }

    public void updateEmployeeSalary(Long id, BigDecimal newSalary)
            throws EmployeeNotFoundException, InvalidDataException {

        if (newSalary == null || newSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException("Invalid salary");
        }

        for (int i = 0; i < employees.length; i++) {
            if (employees[i].getId().equals(id)) {
                employees[i].setSalary(newSalary);
                Logger.info("Salary updated");
                return;
            }
        }

        throw new EmployeeNotFoundException("Employee not found");
    }

    public void assignTaskToProgrammer(Long programmerId, Task task) {

        if (task == null) {
            return;
        }

        for (int i = 0; i < employees.length; i++) {
            if (employees[i].getId().equals(programmerId) && employees[i] instanceof Programmer) {

                Task[] newTasks = new Task[tasks.length + 1];

                for (int j = 0; j < tasks.length; j++) {
                    newTasks[j] = tasks[j];
                }

                newTasks[tasks.length] = task;
                tasks = newTasks;
                Programmer programmer = (Programmer) employees[i];

                Task[] programmerTasks = programmer.getTasks();
                Task[] newProgrammerTasks = new Task[programmerTasks.length + 1];

                for (int k = 0; k < programmerTasks.length; k++) {
                    newProgrammerTasks[k] = programmerTasks[k];
                }

                newProgrammerTasks[programmerTasks.length] = task;
                programmer.setTasks(newProgrammerTasks);

                return;
            }
        }
    }

    public void completeTask(Long taskId) throws TaskNotFoundException {

        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i].getId().equals(taskId)) {
                tasks[i].setState(State.DONE);
                Logger.info("Task completed");
                return;
            }
        }
        throw new TaskNotFoundException("Task not found");
    }

    public void saveAllData() {

        FileRepository repository = new FileRepository();

        try {
            repository.saveEmployees(employees);
            repository.saveTasks(tasks);
            Logger.info("Data saved successfully");

        } catch (DataSaveException e) {
            System.out.println("Ошибка сохранения данных");
            Logger.error("Error while saving data");
        }
    }

    public void loadAllData() {

        FileRepository repository = new FileRepository();

        try {
            employees = repository.loadEmployees();
            tasks = repository.loadTasks();
            Logger.info("Data loaded successfully");

        } catch (DataLoadException e) {
            System.out.println("Ошибка загрузки данных");
            Logger.error("Error while loading data");
        }
    }

    public void printAllEmployees() {
        for (Employee employee : employees) {
            System.out.printf("Id: %03d%n", employee.getId());
            System.out.printf("Name: %s%n", employee.getName());
            System.out.printf("HireDate: %s%n", employee.getHireDate());
            System.out.printf("Position: %s%n", employee.getPosition());
            System.out.printf(java.util.Locale.US, "Salary: %10.2f%n", employee.getSalary());
            System.out.println();
        }
    }
    public void printProgrammersByGrade(Grade grade){
        for (Employee employee: employees){
            if (employee instanceof Programmer) {
                Programmer programmer = (Programmer) employee;
                if (programmer.getGrade() == grade) {
                    System.out.printf("Name: %s%n", programmer.getName());
                    System.out.printf("HireDate: %s%n", programmer.getHireDate());
                    System.out.printf("Position: %s%n", programmer.getPosition());
                    System.out.printf(java.util.Locale.US, "Salary: %10.2f%n", programmer.getSalary());
                    System.out.printf("Grade: %s%n", programmer.getGrade());
                    System.out.println();
                }
            }
        }
    }
    public Task[] getTasksByEmployeeId(Long employeeId) {
        for (Employee employee : employees) {
            if (employee.getId().equals(employeeId) && employee instanceof Programmer) {
                Programmer programmer = (Programmer) employee;
                return programmer.getTasks();
            }
        }
        return null;
    }
    public Programmer getProgrammerByTaskIdAndManagerId(Long taskId, Long managerId) {
        for (Employee employee : employees) {
            if (employee.getId().equals(managerId) && employee instanceof Manager) {
                Manager manager = (Manager) employee;

                for (Programmer programmer : manager.getProgrammers()) {
                    for (Task task : programmer.getTasks()) {
                        if (task.getId().equals(taskId)) {
                            return programmer;
                        }
                    }
                }
            }
        }
        return null;
    }
    public void printEmployeesWithExperienceMoreThan(int n) {
        boolean found = false;

        for (Employee employee : employees) {

            long experience = ChronoUnit.YEARS.between(
                    employee.getHireDate(),
                    LocalDate.now()
            );

            if (experience > n) {
                found = true;

                if (employee instanceof Programmer programmer) {
                    System.out.printf("Id: %03d%n", programmer.getId());
                    System.out.printf("Name: %s%n", programmer.getName());
                    System.out.printf("HireDate: %s%n", programmer.getHireDate());
                    System.out.printf("Position: %s%n", programmer.getPosition());
                    System.out.printf(java.util.Locale.US, "Salary: %10.2f%n", programmer.getSalary());
                    System.out.printf("Grade: %s%n", programmer.getGrade());
                    System.out.println();
                } else {
                    System.out.printf("Id: %03d%n", employee.getId());
                    System.out.printf("Name: %s%n", employee.getName());
                    System.out.printf("HireDate: %s%n", employee.getHireDate());
                    System.out.printf("Position: %s%n", employee.getPosition());
                    System.out.printf(java.util.Locale.US, "Salary: %10.2f%n", employee.getSalary());
                    System.out.println();
                }
            }
        }

        if (!found) {
            System.out.println("Сотрудники не найдены.");
        }
    }
    public void printOverdueTasks() {
        for (Employee employee : employees) {
            if (employee instanceof Programmer) {
                Programmer programmer = (Programmer) employee;

                for (Task task : programmer.getTasks()) {
                    if (task.getEndDay().isBefore(LocalDate.now()) && task.getState() != State.DONE) {
                        System.out.printf("Task id: %03d%n", task.getId());
                        System.out.printf("Task name: %s%n", task.getTaskName());
                        System.out.printf("Start day: %s%n", task.getStartDay());
                        System.out.printf("End day: %s%n", task.getEndDay());
                        System.out.printf("State: %s%n", task.getState());
                        System.out.println();
                    }
                }
            }
        }
    }
}
