package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nedorezova.controller.StudentController;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
