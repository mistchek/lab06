package ru.hse.security;

public class User {

    private String username;
    private String password;
    private Role role;
    private Long employeeId;

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