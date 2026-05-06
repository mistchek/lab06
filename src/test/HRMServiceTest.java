import org.junit.jupiter.api.Test;
import ru.hse.model.Grade;
import ru.hse.model.Programmer;
import ru.hse.model.Task;
import ru.hse.repository.CollectionRepository;
import ru.hse.service.HRMService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HRMServiceTest {

    @Test
    public void testAddEmployee() {

        CollectionRepository repository =
                new CollectionRepository();

        HRMService service =
                new HRMService(repository);

        Programmer programmer =
                new Programmer(
                        "Alex",
                        "Programmer",
                        new BigDecimal("1000"),
                        LocalDate.now(),
                        new Task[0],
                        Grade.JUNIOR
                );

        service.addEmployee(programmer);

        assertEquals(
                1,
                repository.findAllEmployees().size()
        );
    }
}