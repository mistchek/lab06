package ru.hse.security;

import java.util.Map;
import java.util.Set;

public class SecurityConfig {

    private static final Map<Role, Set<Permission>> ROLE_PERMISSIONS = Map.of(
            Role.ADMIN, Set.of(Permission.values()),

            Role.SYSADMIN, Set.of(
                    Permission.VIEW_EMPLOYEES,
                    Permission.EDIT_EMPLOYEES,
                    Permission.MANAGE_USERS
            ),

            Role.HR_MANAGER, Set.of(
                    Permission.VIEW_EMPLOYEES,
                    Permission.EDIT_EMPLOYEES,
                    Permission.VIEW_REPORTS
            ),

            Role.PROJECT_MANAGER, Set.of(
                    Permission.VIEW_EMPLOYEES,
                    Permission.MANAGE_TASKS,
                    Permission.VIEW_REPORTS
            ),

            Role.EMPLOYEE, Set.of(
                    Permission.VIEW_EMPLOYEES
            )
    );

    public static Set<Permission> getPermissions(Role role) {
        return ROLE_PERMISSIONS.getOrDefault(role, Set.of());
    }
}