package ru.nedorezova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.entity.Student;
import ru.nedorezova.exception.StudentNotFoundException;
import ru.nedorezova.repository.CourseRepository;
import ru.nedorezova.repository.EnrollmentRepository;
import ru.nedorezova.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(StudentDtoMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    public List<EnrollmentDto> getStudentEnrollment(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    // Метод для записи на курс
    public void enrollStudent(Long studentId, Long courseId) {
        Student student = getStudent(studentId);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Проверка окна доступности выбора курсов
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        if (!isEnrollmentWindowOpen(course, now)) {
            throw new EnrollmentException("Enrollment window is closed.");
        }

        // Проверка количества свободных мест
        if (course.getAvailableSlots() == 0) {
            throw new EnrollmentException("No available slots on this course.");
        }

        // Добавление записи в таблицу enrollments
        Enrollment enrollment = new Enrollment(student, course);
        enrollmentRepository.save(enrollment);

        // Уменьшение количества доступных мест на курсе
        course.setAvailableSlots(course.getAvailableSlots() - 1);
        courseRepository.save(course);
    }

    // Проверка окна доступности выбора курсов
    private boolean isEnrollmentWindowOpen(Course course, LocalDateTime now) {
        LocalDateTime start = course.getEnrollmentStart();
        LocalDateTime end = course.getEnrollmentEnd();
        return now.isAfter(start) && now.isBefore(end);
    }
}