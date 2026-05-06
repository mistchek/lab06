package ru.hse.repository;

import ru.hse.model.Employee;
import ru.hse.model.Task;
import ru.hse.exception.DataLoadException;
import ru.hse.exception.DataSaveException;

import java.io.*;
import java.util.List;

public class FileRepository {

    public void saveEmployees(List<Employee> employees) throws DataSaveException {
        try {
            createBackup("employees.dat");
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream("employees.dat"));

            out.writeObject(employees);
            out.close();

        } catch (IOException e) {
            throw new DataSaveException("Ошибка сохранения сотрудников");
        }
    }

    public List<Employee> loadEmployees() throws DataLoadException {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream("employees.dat"));

            List<Employee> employees = (List<Employee>) in.readObject();
            in.close();

            return employees;

        } catch (Exception e) {
            throw new DataLoadException("Ошибка загрузки сотрудников");
        }
    }

    public void saveTasks(List<Task> tasks) throws DataSaveException {
        try {
            createBackup("tasks.dat");
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream("tasks.dat"));

            out.writeObject(tasks);
            out.close();

        } catch (IOException e) {
            throw new DataSaveException("Ошибка сохранения задач");
        }
    }

    public List<Task> loadTasks() throws DataLoadException {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream("tasks.dat"));

            List<Task> tasks = (List<Task>) in.readObject();
            in.close();

            return tasks;

        } catch (Exception e) {
            throw new DataLoadException("Ошибка загрузки задач");
        }
    }
    private void createBackup(String fileName) {
        File originalFile = new File(fileName);

        if (!originalFile.exists()) {
            return;
        }

        File backupFile = new File(fileName + ".bak");

        try {
            FileInputStream in = new FileInputStream(originalFile);
            FileOutputStream out = new FileOutputStream(backupFile);

            int data;
            while ((data = in.read()) != -1) {
                out.write(data);
            }

            in.close();
            out.close();

        } catch (IOException e) {
            System.out.println("Ошибка создания резервной копии");
        }
    }
}