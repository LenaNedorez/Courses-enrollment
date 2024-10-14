package ru.nedorezova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.entity.Enrollment;
import ru.nedorezova.entity.Student;
import ru.nedorezova.exception.CourseNotFoundException;
import ru.nedorezova.exception.EnrollmentException;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.mappers.CourseMapper;
import ru.nedorezova.mappers.EnrollmentMapper;
import ru.nedorezova.mappers.StudentMapper;
import ru.nedorezova.repository.CourseRepository;
import ru.nedorezova.repository.EnrollmentRepository;
import ru.nedorezova.repository.StudentRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(StudentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public StudentDto getStudent(Long id) throws StudentNotFoundException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Студент не найден"));
        return StudentMapper.INSTANCE.toDto(student);
    }

    public List<EnrollmentDto> getStudentEnrollment(Long studentId) {
        return enrollmentRepository
                .findByStudentId(studentId)
                .stream()
                .map(EnrollmentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public CourseDto enrollStudent(Long courseId, Long studentId, String timezone) throws CourseNotFoundException, EnrollmentException, StudentNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден."));

        if (course.getCurrentEnrollment() >= course.getCapacity()) {
            throw new EnrollmentException("Данный курс заполнен.");
        }

        if (!isEnrollmentWindowOpen(course, timezone)) {
            throw new EnrollmentException("Время регистрации на данный курс истекло.");
        }

        course.getStudents().add(studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден.")));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден."));
        student.getCourses().add(course);

        courseRepository.save(course);
        studentRepository.save(student);

        course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);

        enrollmentRepository.save(enrollment);

        return CourseMapper.INSTANCE.toDto(course);
    }

    private boolean isEnrollmentWindowOpen(Course course, String timezone) {
        LocalDateTime timeOfEnrollment = LocalDateTime.now(ZoneId.of(timezone));
        return course.getStartDate().isBefore(timeOfEnrollment) && course.getEndDate().isAfter(timeOfEnrollment);
    }

}