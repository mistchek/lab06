package ru.hse.model;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Arrays;

public class Manager extends Employee {
    private Programmer[] programmers;

    public Manager(String name, String position, BigDecimal salary,
                   LocalDate hireDate, Programmer[] programmers) {
        super(name, position, salary, hireDate);
        this.programmers = programmers;
    }
    public Programmer[] getProgrammers() {
        return programmers;
    }
    public void setProgrammers(Programmer[] programmers) {
        this.programmers = programmers;
    }
    @Override
    public String toString() {
        return "Manager{" +
                "programmers=" + Arrays.toString(programmers) +
                "}" + super.toString();
    }
}
