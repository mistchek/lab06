package ru.hse.security;

import ru.hse.exception.InvalidDataException;

import java.util.List;

public class AuthenticationService {

    private final List<User> users;
    private User currentUser;

    public AuthenticationService(List<User> users) {
        this.users = users;
    }

    public User login(String username, String password) throws InvalidDataException {
        for (User user : users) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }

        throw new InvalidDataException("Invalid username or password");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean hasRole(Role role) {
        return currentUser != null
                && currentUser.getRole() == role;
    }

    public boolean hasPermission(Permission permission) {
        return currentUser != null
                && SecurityConfig
                .getPermissions(currentUser.getRole())
                .contains(permission);
    }
}