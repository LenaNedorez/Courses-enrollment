package ru.nedorezova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.NotFoundException;
import ru.nedorezova.repository.CourseRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseDtoMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс не найден"));
        return CourseDtoMapper.convertToDto(course);
    }

    public CourseDto enrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс не найден"));

        // Проверка доступности курса
        if (course.getCurrentEnrollment() >= course.getCapacity()) {
            throw new EnrollmentException("Курс заполнен, пожалуйста, выберите другой");
        }

        if (!isEnrollmentWindowOpen(course)) {
            throw new EnrollmentException("Время регистрации на данный курс истекло");
        }

        // Запись студента на курс

        course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
        course = courseRepository.save(course);

        return CourseDtoMapper.convertToDto(course);
    }

//    public void unenrollStudent(Long courseId, Long studentId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new NotFoundException("Такой курс не найден"));
//
//        // ... логика проверки возможности отмены записи (например, проверка даты)
//
//        course.setCurrentEnrollment(course.getCurrentEnrollment() - 1);
//        courseRepository.save(course);
//    }

    private boolean isEnrollmentWindowOpen(Course course) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow")); // Замените на часовой пояс студента
        return course.getStartDate().isBefore(now) && course.getEndDate().isAfter(now);
    }
}