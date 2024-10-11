package ru.nedorezova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.repository.CourseRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseDtoMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourse(Long courseId) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден"));
        return CourseDtoMapper.convertToDto(course);
    }

}