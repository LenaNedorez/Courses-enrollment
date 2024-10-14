package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nedorezova.controller.CourseController;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.service.CourseService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @Test
    public void testGetCourses() {
        List<CourseDto> courses = Arrays.asList(new CourseDto(), new CourseDto());
        when(courseService.getCourses()).thenReturn(courses);

        ResponseEntity<List<CourseDto>> response = courseController.getCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courses, response.getBody());
        verify(courseService, times(1)).getCourses();
    }

    @Test
    public void testGetCourse_Found() throws CourseNotFoundException {
        Long courseId = 1L;
        CourseDto course = new CourseDto();
        when(courseService.getCourse(courseId)).thenReturn(course);

        ResponseEntity<CourseDto> response = courseController.getCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course, response.getBody());
        verify(courseService, times(1)).getCourse(courseId);
    }

    @Test
    public void testGetCourse_NotFound() throws CourseNotFoundException {
        Long courseId = 1L;
        when(courseService.getCourse(courseId)).thenThrow(CourseNotFoundException.class);

        ResponseEntity<CourseDto> response = courseController.getCourse(courseId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(courseService, times(1)).getCourse(courseId);
    }

    @Test
    public void testGetCoursesByStudent_Found() throws StudentNotFoundException {
        Long studentId = 1L;
        List<CourseDto> courses = Arrays.asList(new CourseDto(), new CourseDto());
        when(courseService.getCoursesByStudent(studentId)).thenReturn(courses);

        ResponseEntity<List<CourseDto>> response = courseController.getCoursesByStudent(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courses, response.getBody());
        verify(courseService, times(1)).getCoursesByStudent(studentId);
    }

    @Test
    public void testGetCoursesByStudent_NotFound() throws StudentNotFoundException {
        Long studentId = 1L;
        when(courseService.getCoursesByStudent(studentId)).thenThrow(StudentNotFoundException.class);

        ResponseEntity<List<CourseDto>> response = courseController.getCoursesByStudent(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(courseService, times(1)).getCoursesByStudent(studentId);
    }
}
