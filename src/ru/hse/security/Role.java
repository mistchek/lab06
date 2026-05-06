package ru.hse.security;

import java.util.Set;

public enum Role {

    ADMIN(Set.of(
            Permission.READ_EMPLOYEES,
            Permission.ADD_EMPLOYEE,
            Permission.REMOVE_EMPLOYEE,
            Permission.UPDATE_SALARY,
            Permission.ASSIGN_TASK,
            Permission.COMPLETE_TASK,
            Permission.SAVE_DATA,
            Permission.LOAD_DATA
    )),

    MANAGER(Set.of(
            Permission.READ_EMPLOYEES,
            Permission.ASSIGN_TASK,
            Permission.COMPLETE_TASK
    )),

    VIEWER(Set.of(
            Permission.READ_EMPLOYEES
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}