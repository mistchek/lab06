package ru.hse.security;

public class User {

    private final String username;
    private final String password;
    private final Role role;
    private final Long employeeId;

    public User(String username, String password, Role role, Long employeeId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}