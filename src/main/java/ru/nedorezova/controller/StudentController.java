package ru.nedorezova.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.service.StudentService;

import java.util.List;

@RestController
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.getStudent(id));
        } catch (StudentNotFoundException e){
            logger.error("Ошибка при получении студента с ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
}

    @GetMapping("/students/{studentId}/enrollments")
    public ResponseEntity<List<EnrollmentDto>> getStudentEnrollment(@PathVariable Long studentId) {
            return ResponseEntity.ok(studentService.getStudentEnrollment(studentId));
    }

    @PostMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<CourseDto> enrollStudent(@PathVariable Long studentId,
                                                   @PathVariable Long courseId,
                                                   @RequestParam String timezone) {
        try {
            return ResponseEntity.ok(studentService.enrollStudent(courseId, studentId, timezone));
        } catch (EnrollmentException e) {
            logger.error("Ошибка при записи студента на курс: {}, {}, {}", studentId, courseId, timezone, e);
            return ResponseEntity.badRequest().build();
        } catch (CourseNotFoundException e) {
            logger.error("Ошибка при поиске курса с ID: {}", courseId, e);
            return ResponseEntity.notFound().build();
        }
    }
}
