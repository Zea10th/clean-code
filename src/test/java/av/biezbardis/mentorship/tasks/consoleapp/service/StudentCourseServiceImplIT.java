package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.SchoolManagementSystem;
import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
class StudentCourseServiceImplIT {
    private final StudentCourseServiceImpl service;

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
    StudentCourseServiceImplIT(StudentCourseServiceImpl service) {
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
    void enrollStudentInCourse() {
        service.enrollStudentInCourse(1L, 9L);

        List<Course> coursesByStudentId = service.getCoursesByStudentId(1L);
        List<Student> studentsByCourseId = service.getStudentsByCourseId(9L);

        assertTrue(coursesByStudentId.stream()
                .anyMatch(course -> course.getId() == 9L)
        );
        assertTrue(studentsByCourseId.stream()
                .anyMatch(student -> student.getId() == 1L)
        );
    }

    @Test
    void unenrollStudentFromCourse() {
        service.unenrollStudentFromCourse(10L, 10L);

        List<Course> coursesByStudentId = service.getCoursesByStudentId(10L);
        List<Student> studentsByCourseId = service.getStudentsByCourseId(10L);

        assertFalse(coursesByStudentId.stream()
                .anyMatch(course -> course.getId() == 10L)
        );
        assertFalse(studentsByCourseId.stream()
                .anyMatch(student -> student.getId() == 10L)
        );
    }

    @Test
    void getCoursesByStudentId() {
        List<Course> coursesByStudentId = service.getCoursesByStudentId(10L);

        List<Long> actual = coursesByStudentId.stream()
                .map(Course::getId)
                .sorted()
                .toList();

        assertEquals(Arrays.asList(1L, 2L, 5L, 7L, 10L), actual);
    }

    @Test
    void getStudentsByCourseId() {
        List<Student> studentsByCourseId = service.getStudentsByCourseId(2L);

        List<Long> actual = studentsByCourseId.stream()
                .map(Student::getId)
                .sorted()
                .toList();

        assertEquals(Arrays.asList(2L, 4L, 6L, 8L, 10L), actual);
    }
}