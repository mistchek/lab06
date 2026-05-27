package ru.hse.model;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public class Manager extends Employee {
    private List<Programmer> programmers;

    public Manager(String name, String position, BigDecimal salary,
                   LocalDate hireDate, List<Programmer> programmers) {
        super(name, position, salary, hireDate);
        this.programmers = programmers;
    }
    public List<Programmer> getProgrammers() {
        return programmers;
    }
    public void setProgrammers(List<Programmer> programmers) {
        this.programmers = programmers;
    }
    @Override
    public String toString() {
        return "Manager{" +
                "programmersCount=" + programmers.size() +
                "} " + super.toString();
    }
}
