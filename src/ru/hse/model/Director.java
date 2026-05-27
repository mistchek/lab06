package ru.hse.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Director extends Employee {
    private List<Manager> manager;
    public Director(String name, String position, BigDecimal salary, LocalDate hireDate, List<Manager> manager) {
        super(name, position, salary, hireDate);

        this.manager = manager;
    }

    public List<Manager> getManager() {
        return manager;
    }

    public void setManager(List<Manager> manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Director{" +
                "manager=" + manager +
                '}';
    }
}
