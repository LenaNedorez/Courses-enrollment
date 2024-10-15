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
import ru.nedorezova.mappers.CourseMapper;
import ru.nedorezova.mappers.EnrollmentMapper;
import ru.nedorezova.mappers.StudentMapper;
import ru.nedorezova.repository.CourseRepository;
import ru.nedorezova.repository.EnrollmentRepository;
import ru.nedorezova.repository.StudentRepository;
import ru.nedorezova.service.StudentService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testGetAllStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Иван", "Иванов"),
                new Student(2L, "Петр", "Петров")
        );
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDto> studentDtos = studentService.getAllStudents();

        assertEquals(2, studentDtos.size());
        verify(studentRepository, times(1)).findAll();
        verify(studentMapper, times(2)).toDto(any(Student.class));
    }

    @Test
    public void testGetStudent_Found() throws StudentNotFoundException {
        Long studentId = 1L;
        Student student = new Student(studentId, "Иван", "Иванов");
        StudentDto studentDto = new StudentDto();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.getStudent(studentId);

        assertEquals(studentDto, result);
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapper, times(1)).toDto(student);
    }

    @Test
    public void testGetStudent_NotFound() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudent(studentId));

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapper, never()).toDto(any(Student.class));
    }

    @Test
    public void testGetStudentEnrollment() {
        Long studentId = 1L;
        List<Enrollment> enrollments = Arrays.asList(
                new Enrollment(1L, studentId, 1L),
                new Enrollment(2L, studentId, 2L)
        );
        List<EnrollmentDto> enrollmentDtos = Arrays.asList(new EnrollmentDto(), new EnrollmentDto());
        when(enrollmentRepository.findByStudentId(studentId)).thenReturn(enrollments);
        when(enrollmentMapper.toDto(enrollments.get(0))).thenReturn(enrollmentDtos.get(0));
        when(enrollmentMapper.toDto(enrollments.get(1))).thenReturn(enrollmentDtos.get(1));

        List<EnrollmentDto> result = studentService.getStudentEnrollment(studentId);

        assertEquals(2, result.size());
        assertEquals(enrollmentDtos.get(0), result.get(0));
        assertEquals(enrollmentDtos.get(1), result.get(1));
        verify(enrollmentRepository, times(1)).findByStudentId(studentId);
        verify(enrollmentMapper, times(2)).toDto(any(Enrollment.class));
    }

    @Test
    public void testEnrollStudent_Success() throws EnrollmentException, CourseNotFoundException, StudentNotFoundException {
        Long courseId = 1L;
        Long studentId = 1L;
        String timezone = "Europe/Moscow";
        Course course = new Course(courseId, "Математика", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        Student student = new Student(studentId, "Иван", "Иванов");
        CourseDto courseDto = new CourseDto();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        CourseDto result = studentService.enrollStudent(courseId, studentId, timezone);

        assertEquals(courseDto, result);
        verify(courseRepository, times(1)).findById(courseId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(courseMapper, times(1)).toDto(course);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    public void testEnrollStudent_CourseFull() {
        Long courseId = 1L;
        Long studentId = 1L;
        String timezone = "Europe/Moscow";
        Course course = new Course(courseId, "Математика", 10, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        assertThrows(EnrollmentException.class, () -> studentService.enrollStudent(courseId, studentId, timezone));

        verify(courseRepository, times(1)).findById(courseId);
        verify(studentRepository, never()).findById(studentId);
        verify(courseMapper, never()).toDto(any(Course.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void testEnrollStudent_StudentNotFound() {
        Long courseId = 1L;
        Long studentId = 1L;
        String timezone = "Europe/Moscow";
        Course course = new Course(courseId, "Математика", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.enrollStudent(courseId, studentId, timezone));

        verify(courseRepository, times(1)).findById(courseId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(courseMapper, never()).toDto(any(Course.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

}
