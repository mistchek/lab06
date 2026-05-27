import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.model.Programmer;
import ru.hse.model.Task;
import ru.hse.model.Grade;
import ru.hse.model.State;
import ru.hse.repository.CollectionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionRepositoryTest {

    private CollectionRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new CollectionRepository();
    }

    @Test
    public void testEmployeeCrud() {
        Programmer programmer = new Programmer(
                "Alex",
                "Programmer",
                new BigDecimal("1000"),
                LocalDate.now(),
                new ArrayList<>(),
                Grade.JUNIOR
        );

        repository.saveEmployee(programmer);

        assertEquals(programmer, repository.findEmployeeById(programmer.getId()));
        assertEquals(1, repository.findAllEmployees().size());

        repository.deleteEmployee(programmer.getId());

        assertNull(repository.findEmployeeById(programmer.getId()));
    }

    @Test
    public void testAssignTaskToProgrammer() {
        Programmer programmer = new Programmer(
                "Alex",
                "Programmer",
                new BigDecimal("1000"),
                LocalDate.now(),
                new ArrayList<>(),
                Grade.JUNIOR
        );

        Task task = new Task(
                "Write tests",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                State.IN_PROGRESS
        );

        repository.saveEmployee(programmer);
        repository.saveTask(task);

        repository.assignTaskToProgrammer(
                programmer.getId(),
                task.getId()
        );

        assertEquals(
                1,
                repository.getTasksByProgrammer(programmer.getId()).size()
        );
    }
}