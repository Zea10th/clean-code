package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.SchoolManagementSystem;
import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
class StudentServiceIT {
    private final StudentService service;

    @MockBean
    SchoolManagementSystem schoolManagementSystem;

    @Container
    private static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
                    .withExposedPorts(5432)
                    .withUsername("test")
                    .withPassword("test")
                    .withDatabaseName("test_db")
                    .waitingFor(
                            new LogMessageWaitStrategy()
                                    .withRegEx(".*database system is ready to accept connections.*\\s")
                                    .withTimes(2).withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS)));

    @Autowired
    StudentServiceIT(StudentService service) {
        this.service = service;
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/%s",
                        postgres.getFirstMappedPort(),
                        postgres.getDatabaseName()));
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.flyway.clean-disabled", () -> "false");
        registry.add("spring.flyway.locations", () ->
                "filesystem:src/test/resources/consoleapp/db/migration," +
                        "filesystem:src/test/resources/consoleapp/db/ext");
    }

    @BeforeAll
    static void startContainers() {
        postgres.start();
    }

    @AfterAll
    static void stopContainers() {
        postgres.stop();
    }

    @BeforeEach
    void dropData(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldSaveEntityAndFindEntityById() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(7L);
        service.save(student);

        Student storedStudent = service.findById(11L);
        assertEquals(student.getFirstName(), storedStudent.getFirstName());
        assertEquals(student.getLastName(), storedStudent.getLastName());
        assertEquals(student.getGroupId(), storedStudent.getGroupId());
    }

    @Test
    void shouldUpdateEntity() {
        String initialName = "John";
        String initialLastName = "Doe";
        String updatedName = "Jeremy";
        String updatedLastName = "Valeska";


        Student student = new Student();
        student.setFirstName(initialName);
        student.setLastName(initialLastName);
        student.setGroupId(7L);
        service.save(student);

        student.setId(1L);
        student.setFirstName(updatedName);
        student.setLastName(updatedLastName);
        service.update(student);

        Student updatedStudent = service.findById(1L);
        assertEquals(updatedName, updatedStudent.getFirstName());
        assertEquals(updatedLastName, updatedStudent.getLastName());
    }

    @Test
    void shouldDeleteEntity() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(7L);
        service.save(student);
        List<Student> initStudents = service.findAll();
        service.delete(1L);
        List<Student> oppositeStudents = service.findAll();

        assertNotEquals(initStudents, oppositeStudents);
        assertEquals(10, oppositeStudents.size());
        assertTrue(oppositeStudents.stream()
                .anyMatch(stud ->
                        "John".equals(stud.getFirstName()) &&
                                "Doe".equals(stud.getLastName()) &&
                                stud.getGroupId() == 7L)
        );
    }

    @Test
    void shouldFindAllEntities() {
        for (int i = 1; i < 4; i++) {
            Student student = new Student();
            student.setFirstName("Firstname" + i);
            student.setLastName("Lastname" + i);
            student.setGroupId(3L + i);
            service.save(student);
        }

        List<Student> savedStudents = service.findAll();

        assertEquals(13, savedStudents.size());
        assertEquals("Firstname1", savedStudents.get(10).getFirstName());
        assertEquals("Lastname2", savedStudents.get(11).getLastName());
        assertEquals(6L, savedStudents.get(12).getGroupId());
    }
}