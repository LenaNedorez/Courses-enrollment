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
    @Mock
    private CourseMapper courseMapper;
    @InjectMocks
    private CourseService courseService;

    @Test
    public void testGetCourses() {
        List<Course> courses = Arrays.asList(
                new Course(1L, "Математика", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1)),
                new Course(2L, "Физика", 20, 5, LocalDateTime.now(), LocalDateTime.now().plusMonths(1))
        );
        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDto> courseDtos = courseService.getCourses();

        assertEquals(2, courseDtos.size());
        verify(courseRepository, times(1)).findAll();
        verify(courseMapper, times(2)).toDto(any(Course.class));
    }

    @Test
    public void testGetCourse_Found() throws CourseNotFoundException {
        Long courseId = 1L;
        Course course = new Course(courseId, "Математика", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        CourseDto courseDto = new CourseDto();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        CourseDto result = courseService.getCourse(courseId);

        assertEquals(courseDto, result);
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseMapper, times(1)).toDto(course);
    }

    @Test
    public void testGetCourse_NotFound() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.getCourse(courseId));

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseMapper, never()).toDto(any(Course.class));
    }

    @Test
    public void testGetCoursesByStudent() throws StudentNotFoundException {
        Long studentId = 1L;
        Student student = new Student(studentId, "Иван", "Иванов");
        Course course1 = new Course(1L, "Математика", 30, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        Course course2 = new Course(2L, "Физика", 20, 5, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        student.getCourses().addAll(Arrays.asList(course1, course2));
        CourseDto courseDto1 = new CourseDto();
        CourseDto courseDto2 = new CourseDto();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseMapper.toDto(course1)).thenReturn(courseDto1);
        when(courseMapper.toDto(course2)).thenReturn(courseDto2);

        List<CourseDto> result = courseService.getCoursesByStudent(studentId);

        assertEquals(2, result.size());
        assertTrue(result.contains(courseDto1));
        assertTrue(result.contains(courseDto2));
        verify(studentRepository, times(1)).findById(studentId);
        verify(courseMapper, times(1)).toDto(course1);
        verify(courseMapper, times(1)).toDto(course2);
    }

    @Test
    public void testGetCoursesByStudent_NotFound() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> courseService.getCoursesByStudent(studentId));

        verify(studentRepository, times(1)).findById(studentId);
        verify(courseMapper, never()).toDto(any(Course.class));
    }
}