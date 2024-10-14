package ru.nedorezova.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.service.CourseService;

import java.util.List;

@RestController
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(courseService.getCourse(courseId));
        } catch (CourseNotFoundException e) {
            logger.error("Ошибка при получении курса с ID: {}", courseId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<List<CourseDto>> getCoursesByStudent(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(courseService.getCoursesByStudent(studentId));
        } catch (StudentNotFoundException e) {
            logger.error("Ошибка при получении курсов студента с ID: {}", studentId, e);
            return ResponseEntity.notFound().build();
        }
    }

}