package ru.nedorezova.service;

import org.springframework.stereotype.Service;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.entity.Course;
import ru.nedorezova.entity.Student;

import java.util.stream.Collectors;

@Service
public class StudentDtoMapper {

    public static StudentDto convertToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setSurname(student.getSurname());
        studentDto.setCourseIds(student.getCourseList().stream()
                .map(Course::getId)
                .collect(Collectors.toList()));
        return studentDto;
    }
}
