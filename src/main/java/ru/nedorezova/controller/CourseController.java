package ru.nedorezova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.service.CourseService;

import java.util.List;

@RestController
public class CourseController {

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
            return ResponseEntity.notFound().build();
        }
    }

}