package ru.nedorezova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(courseService.getCourse(courseId));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<CourseDto> enrollStudent(@PathVariable Long studentId,
                                                   @PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(courseService.enrollStudent(courseId, studentId));
        } catch (EnrollmentException e) {
            return ResponseEntity.badRequest().build();
        } catch (CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}