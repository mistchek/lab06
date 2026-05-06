package ru.hse.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class Director extends Employee {
    private Manager[] manager;
    public Director(String name, String position, BigDecimal salary, LocalDate hireDate, Manager[] manager) {
        super(name, position, salary, hireDate);

        this.manager = manager;
    }

    public Manager[] getManager() {
        return manager;
    }

    public void setManager(Manager[] manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Director{" +
                "manager=" + Arrays.toString(manager) +
                '}';
    }
}
