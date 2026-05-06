package ru.hse.model;
import java.time.LocalDate;
import java.io.Serializable;
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Long counter = 1L;
    private Long id;
    private String taskName;
    private LocalDate startDay;
    private LocalDate endDay;
    private State state;

    public Task(String taskName, LocalDate startDay, LocalDate endDay, State state) {
        this.id = counter;
        counter++;
        this.taskName = taskName;
        this.startDay = startDay;
        this.endDay = endDay;
        this.state = state;
    }
    public Long getId() {
        return id;
    }
    public String getTaskName() {
        return taskName;
    }
    public LocalDate getStartDay() {
        return startDay;
    }
    public LocalDate getEndDay() {
        return endDay;
    }
    public State getState() {
        return state;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }
    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName=" + taskName +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", state=" + state +
                '}';
    }
}
