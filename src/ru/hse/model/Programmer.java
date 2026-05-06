package ru.hse.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Programmer extends Employee {
    private Task[] tasks;
    private Grade grade;
    public Programmer(String name, String position, BigDecimal salary,
                      LocalDate hireDay, Task[] tasks, Grade grade) {
        super(name, position, salary, hireDay);
        this.tasks = tasks;
        this.grade = grade;
    }
    public Task[] getTasks() {
        return tasks;
    }
    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }
    public Grade getGrade() {
        return grade;
    }
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
    @Override public String toString() {
        return "Programmers{" +
                "tasks =" + java.util.Arrays.toString(tasks) +
                ", grade=" + grade +
                "} " + super.toString();
    }
}
