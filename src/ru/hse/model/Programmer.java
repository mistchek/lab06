package ru.hse.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Programmer extends Employee {
    private List<Task> tasks;
    private Grade grade;
    public Programmer(String name, String position, BigDecimal salary,
                      LocalDate hireDay, List<Task> tasks, Grade grade) {
        super(name, position, salary, hireDay);
        this.tasks = tasks;
        this.grade = grade;
    }
    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    public Grade getGrade() {
        return grade;
    }
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
    @Override
    public String toString() {
        return "Programmer{" +
                "tasksCount=" + tasks.size() +
                ", grade=" + grade +
                "} " + super.toString();
    }
}
