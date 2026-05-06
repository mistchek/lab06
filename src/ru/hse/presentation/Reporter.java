package ru.hse.presentation;
import ru.hse.exception.EmployeeNotFoundException;
import ru.hse.exception.InvalidDataException;
import ru.hse.exception.TaskNotFoundException;
import ru.hse.model.Employee;
import ru.hse.model.Programmer;
import ru.hse.model.Task;
import ru.hse.service.HRMService;
import ru.hse.security.SecurityService;
import ru.hse.security.User;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import ru.hse.security.Permission;

public class Reporter {
    private HRMService hrmService;
    private SecurityService securityService;
    private User currentUser;

    public Reporter(HRMService hrmService, SecurityService securityService) {
        this.hrmService = hrmService;
        this.securityService = securityService;
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Login: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = securityService.login(username, password);

        if (currentUser == null) {
            System.out.println("Invalid login or password");
            return;
        }

        System.out.println("Welcome, " + currentUser.getUsername());

        while (true) {
            System.out.println("=== HRM System Menu ===");
            System.out.println("1. Show all employees");
            System.out.println("2. Show programmers by grade");
            System.out.println("3. Show overdue tasks");
            System.out.println("4. Tasks by employee id");
            System.out.println("5. Programmer by task id and manager id");
            System.out.println("6. Employees with experience more than N years");
            System.out.println("7. Add employee");
            System.out.println("8. Remove employee");
            System.out.println("9. Update salary");
            System.out.println("10. Assign task");
            System.out.println("11. Complete task");
            System.out.println("12. Save data");
            System.out.println("13. Load data");
            System.out.println("14. Employee count by position");
            System.out.println("15. Employee with max salary");
            System.out.println("16. Average salary by position");
            System.out.println("17. Exit");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }

            if (choice == 1) {
                hrmService.printAllEmployees();
            }

            if (choice == 2) {
                System.out.println("Enter grade:");
                String gradeInput = scanner.nextLine();
                hrmService.printProgrammersByGrade(
                        ru.hse.model.Grade.valueOf(gradeInput)
                );
            }

            if (choice == 3) {
                hrmService.printOverdueTasks();
            }

            if (choice == 4) {
                System.out.print("Введите id сотрудника: ");
                Long employeeId = Long.parseLong(scanner.nextLine());

                List<Task> tasks = hrmService.getTasksByEmployeeId(employeeId);

                if (tasks != null) {
                    for (Task task : tasks) {
                        System.out.printf("Task id: %03d%n", task.getId());
                        System.out.printf("Task name: %s%n", task.getTaskName());
                        System.out.printf("Start day: %s%n", task.getStartDay());
                        System.out.printf("End day: %s%n", task.getEndDay());
                        System.out.printf("State: %s%n", task.getState());
                        System.out.println();
                    }
                } else {
                    System.out.println("Сотрудник не найден или задач нет.");
                }
            }

            if (choice == 5) {
                System.out.print("Введите taskId: ");
                Long taskId = Long.parseLong(scanner.nextLine());

                System.out.print("Введите managerId: ");
                Long managerId = Long.parseLong(scanner.nextLine());

                Programmer programmer = hrmService.getProgrammerByTaskIdAndManagerId(taskId, managerId);

                if (programmer != null) {
                    System.out.printf("Id: %03d%n", programmer.getId());
                    System.out.printf("Name: %s%n", programmer.getName());
                    System.out.printf("HireDate: %s%n", programmer.getHireDate());
                    System.out.printf("Position: %s%n", programmer.getPosition());
                    System.out.printf(java.util.Locale.US, "Salary: %10.2f%n", programmer.getSalary());
                    System.out.printf("Grade: %s%n", programmer.getGrade());
                    System.out.println();
                } else {
                    System.out.println("Программист не найден.");
                }
            }

            if (choice == 6) {
                System.out.print("Введите N (лет опыта): ");
                int n = Integer.parseInt(scanner.nextLine());
                hrmService.printEmployeesWithExperienceMoreThan(n);
            }

            if (
                    choice == 7
                            &&
                            !securityService.hasPermission(
                                    currentUser,
                                    Permission.ADD_EMPLOYEE
                            )
            ) {
                System.out.println("Access denied");
                continue;
            }

            if (choice == 7) {
                System.out.println("Choose employee type:");
                System.out.println("1. Manager");
                System.out.println("2. Programmer");

                int employeeType = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter employee name: ");
                String name = scanner.nextLine();

                System.out.print("Enter position: ");
                String position = scanner.nextLine();

                System.out.print("Enter salary: ");
                java.math.BigDecimal salary = new java.math.BigDecimal(scanner.nextLine());

                System.out.print("Enter hire year: ");
                int year = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter hire month: ");
                int month = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter hire day: ");
                int day = Integer.parseInt(scanner.nextLine());

                if (employeeType == 1) {
                    ru.hse.model.Manager manager =
                            new ru.hse.model.Manager(
                                    name,
                                    position,
                                    salary,
                                    java.time.LocalDate.of(year, month, day),
                                    new Programmer[0]
                            );

                    hrmService.addEmployee(manager);
                    System.out.println("Employee added successfully");
                }

                if (employeeType == 2) {
                    System.out.print("Enter grade: ");
                    String gradeInput = scanner.nextLine();

                    ru.hse.model.Programmer programmer =
                            new ru.hse.model.Programmer(
                                    name,
                                    position,
                                    salary,
                                    java.time.LocalDate.of(year, month, day),
                                    new Task[0],
                                    ru.hse.model.Grade.valueOf(gradeInput)
                            );

                    hrmService.addEmployee(programmer);
                    System.out.println("Employee added successfully");
                }
            }

            if (
                    choice == 8
                            &&
                            !securityService.hasPermission(
                                    currentUser,
                                    Permission.REMOVE_EMPLOYEE
                            )
            ) {
                System.out.println("Access denied");
                continue;
            }

            if (choice == 8) {
                System.out.print("Enter employee id: ");
                Long id = Long.parseLong(scanner.nextLine());

                try {
                    hrmService.removeEmployeeById(id);
                    System.out.println("Employee removed successfully");
                } catch (EmployeeNotFoundException e) {
                    System.out.println("Employee not found");
                }
            }

            if (
                    choice == 9
                            &&
                            !securityService.hasPermission(
                                    currentUser,
                                    Permission.UPDATE_SALARY
                            )
            ) {
                System.out.println("Access denied");
                continue;
            }

            if (choice == 9) {
                System.out.print("Enter employee id: ");
                Long id = Long.parseLong(scanner.nextLine());

                System.out.print("Enter new salary: ");
                java.math.BigDecimal newSalary = new java.math.BigDecimal(scanner.nextLine());

                try {
                    hrmService.updateEmployeeSalary(id, newSalary);
                    System.out.println("Salary updated successfully");
                } catch (EmployeeNotFoundException e) {
                    System.out.println("Employee not found");
                } catch (InvalidDataException e) {
                    System.out.println("Invalid salary");
                }
            }

            if (
                    choice == 10
                            &&
                            !securityService.hasPermission(
                                    currentUser,
                                    Permission.ASSIGN_TASK
                            )
            ) {
                System.out.println("Access denied");
                continue;
            }

            if (choice == 10) {
                System.out.print("Enter programmer id: ");
                Long programmerId = Long.parseLong(scanner.nextLine());

                System.out.print("Enter task name: ");
                String taskName = scanner.nextLine();

                System.out.print("Enter start year: ");
                int startYear = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter start month: ");
                int startMonth = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter start day: ");
                int startDay = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end year: ");
                int endYear = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end month: ");
                int endMonth = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end day: ");
                int endDay = Integer.parseInt(scanner.nextLine());

                Task task = new Task(
                        taskName,
                        java.time.LocalDate.of(startYear, startMonth, startDay),
                        java.time.LocalDate.of(endYear, endMonth, endDay),
                        ru.hse.model.State.IN_PROGRESS
                );

                hrmService.assignTaskToProgrammer(programmerId, task);
                System.out.println("Task assigned successfully");
            }

            if (
                    choice == 11
                            &&
                            !securityService.hasPermission(
                                    currentUser,
                                    Permission.COMPLETE_TASK
                            )
            ) {
                System.out.println("Access denied");
                continue;
            }

            if (choice == 11) {
                System.out.print("Enter task id: ");
                Long taskId = Long.parseLong(scanner.nextLine());

                try {
                    hrmService.completeTask(taskId);
                    System.out.println("Task completed successfully");
                } catch (TaskNotFoundException e) {
                    System.out.println("Task not found");
                }
            }

            if (choice == 12) {
                hrmService.saveAllData();
                System.out.println("Data saved successfully");
            }

            if (choice == 13) {
                hrmService.loadAllData();
                System.out.println("Data loaded successfully");
            }

            if (choice == 17) {
                break;
            }
            if (choice == 15) {

                Employee employee = hrmService.findEmployeeWithMaxSalary();

                if (employee != null) {
                    System.out.println(employee);
                }
            }
            if (choice == 16) {

                System.out.print("Enter position: ");
                String position = scanner.nextLine();

                double averageSalary =
                        hrmService.getAverageSalaryByPosition(position);

                System.out.println("Average salary: " + averageSalary);
            }
            if (choice == 14) {

                Map<String, Long> counts =
                        hrmService.getEmployeeCountByPosition();

                for (Map.Entry<String, Long> entry : counts.entrySet()) {

                    System.out.println(
                            entry.getKey() + ": " + entry.getValue()
                    );
                }
            }
        }
    }
}