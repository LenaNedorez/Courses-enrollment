package ru.nedorezova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.service.StudentDtoMapper;
import ru.nedorezova.service.StudentService;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.getStudent(id));
        } catch (StudentNotFoundException e){
            return ResponseEntity.notFound().build();
        }
}

    @GetMapping("/students/{id}/enrollments")
    public ResponseEntity<List<EnrollmentDto>> getStudentEnrollment(@PathVariable Long studentId) {
            return ResponseEntity.ok(studentService.getStudentEnrollment(studentId));
    }

    @PostMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<CourseDto> enrollStudent(@PathVariable Long studentId,
                                                   @PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(studentService.enrollStudent(courseId, studentId));
        } catch (EnrollmentException e) {
            return ResponseEntity.badRequest().build();
        } catch (CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
