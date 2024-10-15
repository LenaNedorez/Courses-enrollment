package ru.nedorezova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.entity.Student;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.mappers.CourseMapper;
import ru.nedorezova.repository.CourseRepository;
import ru.nedorezova.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public List<CourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourse(Long courseId) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден"));
        return CourseMapper.INSTANCE.toDto(course);
    }


    public List<CourseDto> getCoursesByStudent(Long studentId) throws StudentNotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден."));

        return student.getCourses().stream()
                .map(CourseMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

}