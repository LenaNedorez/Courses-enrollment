package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nedorezova.controller.StudentController;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void testGetAllStudents() {
        List<StudentDto> students = Arrays.asList(new StudentDto(), new StudentDto());
        when(studentService.getAllStudents()).thenReturn(students);

        ResponseEntity<List<StudentDto>> response = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, response.getBody());
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void testGetStudent_Found() throws StudentNotFoundException {
        Long studentId = 1L;
        StudentDto student = new StudentDto();
        when(studentService.getStudent(studentId)).thenReturn(student);

        ResponseEntity<StudentDto> response = studentController.getStudent(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).getStudent(studentId);
    }

    @Test
    public void testGetStudent_NotFound() throws StudentNotFoundException {
        Long studentId = 1L;
        when(studentService.getStudent(studentId)).thenThrow(StudentNotFoundException.class);

        ResponseEntity<StudentDto> response = studentController.getStudent(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService, times(1)).getStudent(studentId);
    }

    @Test
    public void testEnrollStudent_Success() throws EnrollmentException, CourseNotFoundException, StudentNotFoundException {
        Long studentId = 1L;
        Long courseId = 2L;
        String timezone = "Europe/Moscow";
        CourseDto courseDto = new CourseDto();
        when(studentService.enrollStudent(courseId, studentId, timezone)).thenReturn(courseDto);

        ResponseEntity<CourseDto> response = studentController.enrollStudent(studentId, courseId, timezone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseDto, response.getBody());
        verify(studentService, times(1)).enrollStudent(courseId, studentId, timezone);
    }

    @Test
    public void testEnrollStudent_EnrollmentException() throws EnrollmentException, CourseNotFoundException, StudentNotFoundException {
        Long studentId = 1L;
        Long courseId = 2L;
        String timezone = "Europe/Moscow";
        when(studentService.enrollStudent(courseId, studentId, timezone)).thenThrow(EnrollmentException.class);

        ResponseEntity<CourseDto> response = studentController.enrollStudent(studentId, courseId, timezone);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService, times(1)).enrollStudent(courseId, studentId, timezone);
    }

    @Test
    public void testEnrollStudent_CourseNotFoundException() throws EnrollmentException, CourseNotFoundException, StudentNotFoundException {
        Long studentId = 1L;
        Long courseId = 2L;
        String timezone = "Europe/Moscow";
        when(studentService.enrollStudent(courseId, studentId, timezone)).thenThrow(CourseNotFoundException.class);

        ResponseEntity<CourseDto> response = studentController.enrollStudent(studentId, courseId, timezone);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService, times(1)).enrollStudent(courseId, studentId, timezone);
    }

    @Test
    public void testEnrollStudent_StudentNotFoundException() throws EnrollmentException, CourseNotFoundException, StudentNotFoundException {
        Long studentId = 1L;
        Long courseId = 2L;
        String timezone = "Europe/Moscow";
        when(studentService.enrollStudent(courseId, studentId, timezone)).thenThrow(StudentNotFoundException.class);

        ResponseEntity<CourseDto> response = studentController.enrollStudent(studentId, courseId, timezone);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService, times(1)).enrollStudent(courseId, studentId, timezone);
    }
}
