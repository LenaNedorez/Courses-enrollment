package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.entity.Enrollment;
import ru.nedorezova.entity.Student;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.repository.CourseRepository;
import ru.nedorezova.repository.EnrollmentRepository;
import ru.nedorezova.repository.StudentRepository;
import ru.nedorezova.service.StudentService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testGetAllStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Ivan", "Ivanov"),
                new Student(2L, "Leonid", "Petrov")
        );
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDto> studentDtos = studentService.getAllStudents();

        assertEquals(2, studentDtos.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testGetStudent_Found() throws StudentNotFoundException {
        Long studentId = 1L;
        Student student = new Student(studentId, "Oleg", "Sidorov");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        StudentDto result = studentService.getStudent(studentId);

        assertEquals(studentId, result.getId());
        assertEquals("Oleg", result.getName());
        assertEquals("Sidorov", result.getSurname());

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    public void testGetStudent_NotFound() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudent(studentId));

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    public void testGetStudentEnrollment() {
        Long studentId = 1L;
        List<Enrollment> enrollments = Arrays.asList(
                new Enrollment(1L, studentId, 1L),
                new Enrollment(2L, studentId, 2L)
        );
        when(enrollmentRepository.findByStudentId(studentId)).thenReturn(enrollments);

        List<EnrollmentDto> result = studentService.getStudentEnrollment(studentId);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getCourseId());
        assertEquals(2L, result.get(1).getCourseId());

        verify(enrollmentRepository, times(1)).findByStudentId(studentId);
    }

    @Test
    public void testEnrollStudent_Success() throws EnrollmentException, CourseNotFoundException, StudentNotFoundException {
        Long courseId = 1L;
        Long studentId = 1L;
        String timezone = "Europe/Moscow";
        LocalDateTime now = LocalDateTime.now();
        Course course = new Course(courseId, "Math", 30, 10, now.minusDays(1), now.plusDays(1));
        Student student = new Student(studentId, "Ivan", "Ivanov");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        CourseDto result = studentService.enrollStudent(courseId, studentId, timezone);

        assertNotNull(result);
        verify(courseRepository, times(1)).findById(courseId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    public void testEnrollStudent_CourseFull() {
        Long courseId = 1L;
        Long studentId = 1L;
        String timezone = "Europe/Moscow";
        Course course = new Course(courseId, "History", 10, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        assertThrows(EnrollmentException.class, () -> studentService.enrollStudent(courseId, studentId, timezone));

        verify(courseRepository, times(1)).findById(courseId);
        verify(studentRepository, never()).findById(studentId);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void testEnrollStudent_StudentNotFound() {
        Long courseId = 1L;
        Long studentId = 1L;
        String timezone = "Europe/Moscow";
        Course course = new Course(courseId, "Math", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.enrollStudent(courseId, studentId, timezone));

        verify(courseRepository, times(1)).findById(courseId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

}
