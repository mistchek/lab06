package ru.hse.security;

import java.util.ArrayList;
import java.util.List;

public class SecurityService {

    private List<User> users = new ArrayList<>();

    public SecurityService() {

        users.add(new User(
                "admin",
                "admin123",
                Role.ADMIN,
                null
        ));

        users.add(new User(
                "manager",
                "manager123",
                Role.MANAGER,
                3L
        ));

        users.add(new User(
                "viewer",
                "viewer123",
                Role.VIEWER,
                null
        ));
    }

    public User login(String username, String password) {

        for (User user : users) {

            if (
                    user.getUsername().equals(username)
                            &&
                            user.getPassword().equals(password)
            ) {
                return user;
            }
        }

        return null;
    }

    public boolean hasPermission(User user, Permission permission) {

        return user.getRole()
                .getPermissions()
                .contains(permission);
    }
}