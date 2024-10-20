package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.entity.Student;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.mappers.CourseMapper;
import ru.nedorezova.repository.CourseRepository;
import ru.nedorezova.repository.StudentRepository;
import ru.nedorezova.service.CourseService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    public void testGetCourses() {
        List<Course> courses = Arrays.asList(
                new Course(1L, "Math", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1)),
                new Course(2L, "History", 20, 5, LocalDateTime.now(), LocalDateTime.now().plusMonths(1))
        );
        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDto> courseDtos = courseService.getCourses();

        assertEquals(2, courseDtos.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    public void testGetCourse_Found() throws CourseNotFoundException {
        Long courseId = 1L;
        Course course = new Course(courseId, "Math", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));

        CourseDto courseDto = CourseMapper.INSTANCE.toDto(course);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        CourseDto result = courseService.getCourse(courseId);

        assertEquals(courseDto.getId(), result.getId());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    public void testGetCourse_NotFound() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.getCourse(courseId));

        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    public void testGetCoursesByStudent() throws StudentNotFoundException {
        Long studentId = 1L;
        Student student = new Student(studentId, "Ivan", "Ivanov");
        Course course1 = new Course(1L, "Math", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        Course course2 = new Course(2L, "Russian language", 20, 5, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        student.getCourses().addAll(Arrays.asList(course1, course2));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        List<CourseDto> result = courseService.getCoursesByStudent(studentId);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> CourseMapper.INSTANCE.toDto(course1).getId().equals(c.getId())));
        assertTrue(result.stream().anyMatch(c -> CourseMapper.INSTANCE.toDto(course2).getId().equals(c.getId())));

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    public void testGetCoursesByStudent_NotFound() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> courseService.getCoursesByStudent(studentId));

        verify(studentRepository, times(1)).findById(studentId);
    }
}