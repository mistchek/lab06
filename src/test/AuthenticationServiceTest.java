import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.exception.InvalidDataException;
import ru.hse.security.AuthenticationService;
import ru.hse.security.Permission;
import ru.hse.security.Role;
import ru.hse.security.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        authenticationService = new AuthenticationService(
                List.of(
                        new User("admin", "123", Role.ADMIN, null),
                        new User("employee", "123", Role.EMPLOYEE, 1L)
                )
        );
    }

    @Test
    public void testSuccessfulLogin() throws InvalidDataException {
        User user = authenticationService.login("admin", "123");

        assertEquals("admin", user.getUsername());
        assertTrue(authenticationService.hasRole(Role.ADMIN));
    }

    @Test
    public void testInvalidLogin() {
        assertThrows(
                InvalidDataException.class,
                () -> authenticationService.login("admin", "wrong")
        );
    }

    @Test
    public void testPermission() throws InvalidDataException {
        authenticationService.login("admin", "123");

        assertTrue(
                authenticationService.hasPermission(Permission.VIEW_EMPLOYEES)
        );
    }
}