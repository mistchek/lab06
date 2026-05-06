package ru.hse.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;

public abstract class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Long counter = 1L;
    private Long id;
    private String name;
    private String position;
    private BigDecimal salary;
    private LocalDate hireDate;

    public Employee(String name, String position, BigDecimal salary, LocalDate hireDate) {
        this.id = counter;
        counter++;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.hireDate = hireDate;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPosition() {
        return position;
    }
    public BigDecimal getSalary() {
        return salary;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name=" + name +
                ", position=" + position +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                '}';
    }
}
