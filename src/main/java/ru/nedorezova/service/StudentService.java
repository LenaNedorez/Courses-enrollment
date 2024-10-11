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
                .map(StudentDtoMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public Student getStudent(Long id) throws StudentNotFoundException {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    public List<EnrollmentDto> getStudentEnrollment(Long studentId) {
        return enrollmentRepository
                .findByStudentId(studentId)
                .stream()
                .map(EnrollmentDtoMapper::convertToDto)
                .collect(Collectors.toList());;
    }

    public CourseDto enrollStudent(Long courseId, Long studentId) throws CourseNotFoundException, EnrollmentException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден"));

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

    private boolean isEnrollmentWindowOpen(Course course) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        return course.getStartDate().isBefore(now) && course.getEndDate().isAfter(now);
    }

}