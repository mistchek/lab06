package ru.hse;
import ru.hse.model.*;
import ru.hse.presentation.Reporter;
import ru.hse.security.SecurityService;
import ru.hse.service.HRMService;
import java.math.BigDecimal;
import java.time.LocalDate;
import ru.hse.model.Employee;
import ru.hse.model.Manager;
import ru.hse.model.Programmer;
import ru.hse.model.Task;
import ru.hse.model.State;
import ru.hse.repository.CollectionRepository;
import ru.hse.util.Logger;

public class HRMApplication {
    public static void main(String[] args) {
        Logger.info("Application started");

        Task task1 = new Task(
                "Create a website",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 5, 5),
                State.IN_PROGRESS
        );

        Task task2 = new Task(
                "Write tests",
                LocalDate.of(2026, 2, 20),
                LocalDate.of(2026, 2, 26),
                State.DONE
        );

        Task task3 = new Task(
                "Fix bug",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 3, 7),
                State.IN_PROGRESS
        );

        Programmer programmer1 = new Programmer(
                "Dmitriy Petrov",
                "Backend Java Developer",
                new BigDecimal("150000"),
                LocalDate.of(2021, 5, 1),
                new Task[]{task1, task2},
                Grade.SENIOR
        );

        Programmer programmer2 = new Programmer(
                "Alena Ivanova",
                "Mobile Java Developer",
                new BigDecimal("100000"),
                LocalDate.of(2023, 2, 1),
                new Task[]{task3},
                Grade.MIDDLE
        );

        Manager manager1 = new Manager(
                "Mark Sidorov",
                "Team Lead",
                new BigDecimal("165000"),
                LocalDate.of(2020, 8, 14),
                new Programmer[]{programmer1, programmer2}
        );

        Director director1 = new Director(
                "Matvey K",
                "Team Lead",
                new BigDecimal("165000"),
                LocalDate.of(2020, 8, 14),
                new Manager[]{manager1}
        );

        CollectionRepository repository = new CollectionRepository();

        repository.saveTask(task1);
        repository.saveTask(task2);
        repository.saveTask(task3);

        repository.assignTaskToProgrammer(programmer1.getId(), task1.getId());
        repository.assignTaskToProgrammer(programmer1.getId(), task2.getId());
        repository.assignTaskToProgrammer(programmer2.getId(), task3.getId());

        repository.saveEmployee(programmer1);
        repository.saveEmployee(programmer2);
        repository.saveEmployee(manager1);
        repository.saveEmployee(director1);

        HRMService hrmService = new HRMService(repository);
        SecurityService securityService = new SecurityService();


        java.io.File employeesFile = new java.io.File("employees.dat");
        java.io.File tasksFile = new java.io.File("tasks.dat");

        if (employeesFile.exists() && tasksFile.exists()) {
            hrmService.loadAllData();
        }


        Reporter reporter = new Reporter(hrmService, securityService);

        boolean debugMode = false;

        for (String arg : args) {
            if (arg.equals("--debug")) {
                debugMode = true;
                break;
            }
        }

        if (debugMode) {
            debugPrintState(hrmService);
        }
        Logger.info("Application closed");

        reporter.show();
    }
    public static void debugPrintState(HRMService hrmService) {
        int managersCount = 0;
        int programmersCount = 0;

        for (Employee employee : hrmService.getRepository().findAllEmployees()) {
            if (employee instanceof Manager) {
                managersCount++;
            }
            if (employee instanceof Programmer) {
                programmersCount++;
            }
        }

        int doneTasks = 0;
        int inProgressTasks = 0;

        for (Task task : hrmService.getRepository().findAllTasks()) {
            if (task.getState() == State.DONE) {
                doneTasks++;
            }
            if (task.getState() == State.IN_PROGRESS) {
                inProgressTasks++;
            }
        }

        System.out.println("=== DEBUG INFO ===");
        System.out.println("Managers: " + managersCount);
        System.out.println("Programmers: " + programmersCount);
        System.out.println("Tasks DONE: " + doneTasks);
        System.out.println("Tasks IN_PROGRESS: " + inProgressTasks);
        System.out.println("Employees file: employees.dat");
        System.out.println("Tasks file: tasks.dat");
    }
}