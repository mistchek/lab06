package ru.hse.presentation;
import ru.hse.exception.EmployeeNotFoundException;
import ru.hse.exception.InvalidDataException;
import ru.hse.exception.TaskNotFoundException;
import ru.hse.model.*;
import ru.hse.service.HRMService;
import ru.hse.security.AuthenticationService;
import ru.hse.security.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import ru.hse.security.Permission;
import ru.hse.service.ServiceResult;
import java.time.LocalDate;

public class Reporter {
    private HRMService hrmService;
    private AuthenticationService securityService;
    private User currentUser;

    public Reporter(HRMService hrmService, AuthenticationService securityService) {
        this.hrmService = hrmService;
        this.securityService = securityService;
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Login: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            currentUser = securityService.login(username, password);
        } catch (InvalidDataException e) {
            System.out.println("Invalid login or password");
            return;
        }

        if (currentUser == null) {
            System.out.println("Invalid login or password");
            return;
        }

        System.out.println("Welcome, " + currentUser.getUsername());

        while (true) {
            System.out.println("=== HRM System Menu ===");

            if (currentUser == null) {
                System.out.println("=== Current user: not logged in ===");
                System.out.println("14. Login");
                System.out.println("17. Exit");
            } else {
                System.out.printf(
                        "=== Current user: %s (%s) ===%n",
                        currentUser.getUsername(),
                        currentUser.getRole()
                );

                System.out.println("1. Show all employees");
                System.out.println("2. Show filter menu (lambda/stream)");

                if (securityService.hasPermission(Permission.EDIT_EMPLOYEES)) {
                    System.out.println("3. Add new employee");
                    System.out.println("4. Update employee salary");
                }

                if (securityService.hasPermission(Permission.DELETE_EMPLOYEES)) {
                    System.out.println("5. Remove employee by ID");
                }

                if (securityService.hasPermission(Permission.MANAGE_TASKS)) {
                    System.out.println("6. Assign task to programmer");
                    System.out.println("7. Complete task");
                }

                System.out.println("8. Save data to file");
                System.out.println("9. Load data from file");

                System.out.println("--- Advanced Reports ---");
                System.out.println("10. Show employees grouped by grade (Stream)");
                System.out.println("11. Show average salary by position (Stream)");
                System.out.println("12. Show employee with max salary (Stream)");
                System.out.println("13. Show overdue tasks (Stream)");

                System.out.println("--- Security ---");
                System.out.println("15. Logout");
                System.out.println("16. Show current user info");
                System.out.println("17. Exit");
            }

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }

            if (choice == 1) {

                ServiceResult<List<Employee>> result =
                        hrmService.printAllEmployees();

                if (result.isSuccess()) {

                    for (Employee employee : result.getData()) {

                        System.out.printf(
                                "Id: %03d%n",
                                employee.getId()
                        );

                        System.out.printf(
                                "Name: %s%n",
                                employee.getName()
                        );

                        System.out.printf(
                                "HireDate: %s%n",
                                employee.getHireDate()
                        );

                        System.out.printf(
                                "Position: %s%n",
                                employee.getPosition()
                        );

                        System.out.printf(
                                java.util.Locale.US,
                                "Salary: %10.2f%n",
                                employee.getSalary()
                        );

                        System.out.println();
                    }

                } else {
                    System.out.println(
                            result.getErrorMessage()
                    );
                }
            }


            if (choice == 2) {

                System.out.println("=== Filter Menu ===");
                System.out.println("1. Programmers only");
                System.out.println("2. Employees with high salary");
                System.out.println("3. Employees with experience > N years");

                int filterChoice =
                        Integer.parseInt(scanner.nextLine());

                List<Employee> filteredEmployees =
                        new ArrayList<>();

                if (filterChoice == 1) {

                    filteredEmployees =
                            hrmService.filterEmployees(
                                    employee -> employee instanceof Programmer
                            );
                }

                if (filterChoice == 2) {

                    System.out.print("Enter minimum salary: ");
                    BigDecimal minSalary =
                            new BigDecimal(scanner.nextLine());

                    filteredEmployees =
                            hrmService.filterEmployees(
                                    (Employee employee) ->
                                            employee.getSalary()
                                                    .compareTo(minSalary) > 0
                            );
                }

                if (filterChoice == 3) {

                    System.out.print("Enter years: ");
                    int years = Integer.parseInt(scanner.nextLine());

                    filteredEmployees =
                            hrmService.filterEmployees(
                                    (Employee employee) ->
                                            employee.getHireDate()
                                                    .until(LocalDate.now())
                                                    .getYears() > years
                            );
                }

                for (Employee employee : filteredEmployees) {

                    System.out.printf(
                            "Id: %03d | Name: %s | Position: %s%n",
                            employee.getId(),
                            employee.getName(),
                            employee.getPosition()
                    );
                }
            }

            if (choice == 3) {

                System.out.println("Choose employee type:");
                System.out.println("1. Manager");
                System.out.println("2. Programmer");

                int employeeType =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter employee name: ");
                String name = scanner.nextLine();

                System.out.print("Enter position: ");
                String position = scanner.nextLine();

                System.out.print("Enter salary: ");
                BigDecimal salary =
                        new BigDecimal(scanner.nextLine());

                System.out.print("Enter hire year: ");
                int year =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter hire month: ");
                int month =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter hire day: ");
                int day =
                        Integer.parseInt(scanner.nextLine());

                LocalDate hireDate =
                        LocalDate.of(year, month, day);

                if (employeeType == 1) {

                    Manager manager =
                            new Manager(
                                    name,
                                    position,
                                    salary,
                                    hireDate,
                                    new ArrayList<>()
                            );

                    ServiceResult<Employee> result =
                            hrmService.addEmployee(manager);

                    if (result.isSuccess()) {
                        System.out.println("Employee added successfully");
                    } else {
                        System.out.println(result.getErrorMessage());
                    }

                } else if (employeeType == 2) {

                    System.out.print("Enter grade: ");
                    String gradeInput = scanner.nextLine();

                    try {
                        Programmer programmer =
                                new Programmer(
                                        name,
                                        position,
                                        salary,
                                        hireDate,
                                        new ArrayList<>(),
                                        Grade.valueOf(
                                                gradeInput.toUpperCase()
                                        )
                                );

                        ServiceResult<Employee> result =
                                hrmService.addEmployee(programmer);

                        if (result.isSuccess()) {
                            System.out.println("Employee added successfully");
                        } else {
                            System.out.println(result.getErrorMessage());
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid grade");
                    }

                } else {
                    System.out.println("Invalid employee type");
                }
            }

            if (choice == 4) {

                System.out.print("Enter employee id: ");
                Long id =
                        Long.parseLong(scanner.nextLine());

                System.out.print("Enter new salary: ");
                BigDecimal newSalary =
                        new BigDecimal(scanner.nextLine());

                ServiceResult<Employee> result =
                        hrmService.updateSalary(id, newSalary);

                if (result.isSuccess()) {
                    System.out.println("Salary updated successfully");
                } else {
                    System.out.println(result.getErrorMessage());
                }
            }

            if (choice == 5) {

                System.out.print("Enter employee id: ");
                Long id = Long.parseLong(scanner.nextLine());

                ServiceResult<Employee> result =
                        hrmService.removeEmployeeById(id);

                if (result.isSuccess()) {
                    System.out.println("Employee removed successfully");
                } else {
                    System.out.println(result.getErrorMessage());
                }
            }

            if (choice == 6) {

                System.out.print("Enter programmer id: ");
                Long programmerId =
                        Long.parseLong(scanner.nextLine());

                System.out.print("Enter task name: ");
                String taskName =
                        scanner.nextLine();

                System.out.print("Enter start year: ");
                int startYear =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter start month: ");
                int startMonth =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter start day: ");
                int startDay =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end year: ");
                int endYear =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end month: ");
                int endMonth =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end day: ");
                int endDay =
                        Integer.parseInt(scanner.nextLine());

                Task task =
                        new Task(
                                taskName,
                                LocalDate.of(startYear, startMonth, startDay),
                                LocalDate.of(endYear, endMonth, endDay),
                                State.IN_PROGRESS
                        );

                ServiceResult<Task> result =
                        hrmService.assignTaskToProgrammer(
                                programmerId,
                                task
                        );

                if (result.isSuccess()) {
                    System.out.println("Task assigned successfully");
                } else {
                    System.out.println(result.getErrorMessage());
                }
            }

            if (choice == 7) {

                System.out.print("Enter task id: ");
                Long taskId =
                        Long.parseLong(scanner.nextLine());

                ServiceResult<Task> result =
                        hrmService.completeTask(taskId);

                if (result.isSuccess()) {
                    System.out.println("Task completed successfully");
                } else {
                    System.out.println(result.getErrorMessage());
                }
            }

            if (choice == 8) {
                hrmService.saveAllData();
                System.out.println("Data saved successfully");
            }

            if (choice == 9) {
                hrmService.loadAllData();
                System.out.println("Data loaded successfully");
            }

            if (choice == 10) {

                System.out.println("Enter grade:");
                String gradeInput = scanner.nextLine();

                try {

                    ServiceResult<List<Programmer>> result =
                            hrmService.printProgrammersByGrade(
                                    Grade.valueOf(
                                            gradeInput.toUpperCase()
                                    )
                            );

                    if (result.isSuccess()) {

                        for (Programmer programmer :
                                result.getData()) {

                            System.out.printf(
                                    "Id: %03d%n",
                                    programmer.getId()
                            );

                            System.out.printf(
                                    "Name: %s%n",
                                    programmer.getName()
                            );

                            System.out.printf(
                                    "HireDate: %s%n",
                                    programmer.getHireDate()
                            );

                            System.out.printf(
                                    "Position: %s%n",
                                    programmer.getPosition()
                            );

                            System.out.printf(
                                    java.util.Locale.US,
                                    "Salary: %10.2f%n",
                                    programmer.getSalary()
                            );

                            System.out.printf(
                                    "Grade: %s%n",
                                    programmer.getGrade()
                            );

                            System.out.println();
                        }

                    } else {
                        System.out.println(
                                result.getErrorMessage()
                        );
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid grade");
                }
            }

            if (choice == 11) {

                System.out.print("Enter position: ");
                String position = scanner.nextLine();

                ServiceResult<Double> result =
                        hrmService.getAverageSalaryByPosition(position);

                if (result.isSuccess()) {

                    System.out.printf(
                            java.util.Locale.US,
                            "Average salary: %10.2f%n",
                            result.getData()
                    );

                } else {

                    System.out.println(result.getErrorMessage());
                }
            }

            if (choice == 12) {

                ServiceResult<Employee> result =
                        hrmService.findEmployeeWithMaxSalary();

                if (result.isSuccess()) {
                    Employee employee = result.getData();

                    System.out.printf("Id: %03d%n", employee.getId());
                    System.out.printf("Name: %s%n", employee.getName());
                    System.out.printf("HireDate: %s%n", employee.getHireDate());
                    System.out.printf("Position: %s%n", employee.getPosition());
                    System.out.printf(
                            java.util.Locale.US,
                            "Salary: %10.2f%n",
                            employee.getSalary()
                    );

                    System.out.println();
                } else {
                    System.out.println(result.getErrorMessage());
                }
            }

            if (choice == 13) {

                ServiceResult<List<Task>> result =
                        hrmService.printOverdueTasks();

                if (result.isSuccess()) {

                    for (Task task : result.getData()) {

                        System.out.printf("Task id: %03d%n",
                                task.getId());

                        System.out.printf("Task name: %s%n",
                                task.getTaskName());

                        System.out.printf("Start day: %s%n",
                                task.getStartDay());

                        System.out.printf("End day: %s%n",
                                task.getEndDay());

                        System.out.printf("State: %s%n",
                                task.getState());

                        System.out.println();
                    }

                } else {

                    System.out.println(result.getErrorMessage());
                }
            }

            if (choice == 14) {

                System.out.print("Login: ");
                String loginUsername = scanner.nextLine();

                System.out.print("Password: ");
                String loginPassword = scanner.nextLine();

                try {

                    currentUser =
                            securityService.login(
                                    loginUsername,
                                    loginPassword
                            );

                    System.out.println(
                            "Welcome, " +
                                    currentUser.getUsername()
                    );

                } catch (InvalidDataException e) {

                    System.out.println(
                            "Invalid login or password"
                    );
                }
            }

            if (choice == 15) {

                securityService.logout();
                currentUser = null;

                System.out.println("Logged out successfully");
            }

            if (choice == 16) {

                if (currentUser == null) {
                    System.out.println("User is not logged in");
                } else {
                    System.out.println("Username: " + currentUser.getUsername());
                    System.out.println("Role: " + currentUser.getRole());
                    System.out.println("Employee id: " + currentUser.getEmployeeId());
                }
            }

            if (choice == 17) {
                break;
            }

            //if (choice == 20) {

               // System.out.print("Введите id сотрудника: ");

               // Long employeeId =
                 //       Long.parseLong(scanner.nextLine());

              //  ServiceResult<List<Task>> result =
              //          hrmService.getTasksByEmployeeId(employeeId);

                //if (result.isSuccess()) {

                 //   for (Task task : result.getData()) {

                 //       System.out.printf("Task id: %03d%n",
                           //     task.getId());

                      //  System.out.printf("Task name: %s%n",
                      //          task.getTaskName());

                     //   System.out.printf("Start day: %s%n",
                      //          task.getStartDay());

                       // System.out.printf("End day: %s%n",
                       //         task.getEndDay());

                      //  System.out.printf("State: %s%n",
                      //          task.getState());

                      //  System.out.println();
                  //  }

             //   } else {

              //      System.out.println(result.getErrorMessage());
              //  }
          //  }

        //    if (choice == 21) {

          //      System.out.print("Введите taskId: ");
           //     Long taskId = Long.parseLong(scanner.nextLine());

           //     System.out.print("Введите managerId: ");
           //     Long managerId = Long.parseLong(scanner.nextLine());

           //     ServiceResult<Programmer> result =
           //             hrmService.getProgrammerByTaskIdAndManagerId(taskId, managerId);

            //    if (result.isSuccess()) {

             //       Programmer programmer = result.getData();

              //      System.out.printf("Id: %03d%n", programmer.getId());
             //       System.out.printf("Name: %s%n", programmer.getName());
              //      System.out.printf("HireDate: %s%n", programmer.getHireDate());
              //      System.out.printf("Position: %s%n", programmer.getPosition());
              //      System.out.printf(java.util.Locale.US,
                  //          "Salary: %10.2f%n",
                  //          programmer.getSalary());
                 //   System.out.printf("Grade: %s%n", programmer.getGrade());

                //    System.out.println();

             //   } else {
             //       System.out.println(result.getErrorMessage());
             //   }
          //  }

          //  if (choice == 22) {

          //      System.out.print("Введите N (лет опыта): ");

            //    int n =
            //            Integer.parseInt(scanner.nextLine());

            //    ServiceResult<List<Employee>> result =
             //           hrmService.printEmployeesWithExperienceMoreThan(n);

            //    if (result.isSuccess()) {

               //     for (Employee employee : result.getData()) {

                //        System.out.printf("Id: %03d%n",
                 //               employee.getId());

                  //      System.out.printf("Name: %s%n",
                   //             employee.getName());

                   //     System.out.printf("HireDate: %s%n",
                     //           employee.getHireDate());

                    //    System.out.printf("Position: %s%n",
                    //            employee.getPosition());

                    //    System.out.printf(
                    //            java.util.Locale.US,
                    //            "Salary: %10.2f%n",
                     //           employee.getSalary()
                     //   );

                     //   System.out.println();
                 //   }

              //  } else {

              //      System.out.println(
                 //           result.getErrorMessage()
                 //   );
              //  }
          //  }



          //  if (choice == 19) {

             //   ServiceResult<Map<String, Long>> result =
             //           hrmService.getEmployeeCountByPosition();

            //    if (result.isSuccess()) {

             //       for (Map.Entry<String, Long> entry :
             //               result.getData().entrySet()) {

              //          System.out.println(
                //                entry.getKey() + ": " + entry.getValue()
                //        );
                //    }

            //    } else {

             //       System.out.println(result.getErrorMessage());
            //    }
           // }

            // if (choice == 18) {

                //if (!securityService.hasPermission(Permission.EDIT_EMPLOYEES)) {
                    //System.out.println("Access denied");
                //} else {

                   // System.out.print("Enter employee id: ");
                   // Long id = scanner.nextLong();

                   // System.out.print("Enter new salary: ");
                   // BigDecimal salary = scanner.nextBigDecimal();

                   // hrmService.updateData(id, salary);

                   // System.out.println("Employee salary updated");
                //}
            //}
        }
    }
}