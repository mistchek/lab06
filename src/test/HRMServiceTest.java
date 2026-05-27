import org.junit.jupiter.api.Test;
import ru.hse.exception.InvalidDataException;
import ru.hse.model.Employee;
import ru.hse.model.Grade;
import ru.hse.model.Programmer;
import ru.hse.model.Task;
import ru.hse.repository.CollectionRepository;
import ru.hse.security.Role;
import ru.hse.service.HRMService;
import ru.hse.security.AuthenticationService;
import ru.hse.security.User;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import ru.hse.service.ServiceResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HRMServiceTest {

    @Test
    public void testAddEmployee() throws InvalidDataException {

        CollectionRepository repository =
                new CollectionRepository();

        AuthenticationService authenticationService =
                new AuthenticationService(
                        List.of(
                                new User(
                                        "admin",
                                        "123",
                                        Role.ADMIN,
                                        null
                                )
                        )
                );

        authenticationService.login("admin", "123");

        HRMService service =
                new HRMService(repository, authenticationService);

        Programmer programmer =
                new Programmer(
                        "Alex",
                        "Programmer",
                        new BigDecimal("1000"),
                        LocalDate.now(),
                        new ArrayList<>(),
                        Grade.JUNIOR
                );

        service.addEmployee(programmer);

        assertEquals(
                1,
                repository.findAllEmployees().size()
        );
    }
    @Test
    public void testFilterEmployees() throws InvalidDataException {

        CollectionRepository repository = new CollectionRepository();

        AuthenticationService authenticationService =
                new AuthenticationService(
                        List.of(new User("admin", "123", Role.ADMIN, null))
                );

        authenticationService.login("admin", "123");

        HRMService service =
                new HRMService(repository, authenticationService);

        Programmer programmer = new Programmer(
                "Alex",
                "Programmer",
                new BigDecimal("1000"),
                LocalDate.now().minusYears(3),
                new ArrayList<>(),
                Grade.JUNIOR
        );

        service.addEmployee(programmer);

        List<Employee> result =
                service.filterEmployees(employee ->
                        employee.getPosition().equals("Programmer")
                );

        assertEquals(1, result.size());
    }
    @Test
    public void testGetEmployeeCountByPosition() throws InvalidDataException {

        CollectionRepository repository = new CollectionRepository();

        AuthenticationService authenticationService =
                new AuthenticationService(
                        List.of(new User("admin", "123", Role.ADMIN, null))
                );

        authenticationService.login("admin", "123");

        HRMService service =
                new HRMService(repository, authenticationService);

        Programmer programmer = new Programmer(
                "Alex",
                "Programmer",
                new BigDecimal("1000"),
                LocalDate.now(),
                new ArrayList<>(),
                Grade.JUNIOR
        );

        service.addEmployee(programmer);

        ServiceResult<Map<String, Long>> result =
                service.getEmployeeCountByPosition();

        assertEquals(true, result.isSuccess());
        assertEquals(1, result.getData().get("Programmer"));
    }
    @Test
    public void testFindEmployeeWithMaxSalaryEmpty() throws InvalidDataException {

        CollectionRepository repository =
                new CollectionRepository();

        AuthenticationService authenticationService =
                new AuthenticationService(
                        List.of(new User("admin", "123", Role.ADMIN, null))
                );

        authenticationService.login("admin", "123");

        HRMService service =
                new HRMService(repository, authenticationService);

        ServiceResult<Employee> result =
                service.findEmployeeWithMaxSalary();

        assertEquals(false, result.isSuccess());
    }
}