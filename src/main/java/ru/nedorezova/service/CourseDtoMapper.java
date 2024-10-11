package ru.nedorezova.service;

import org.springframework.stereotype.Service;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.entity.Course;
@Service
public class CourseDtoMapper {

    public static CourseDto convertToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setCapacity(course.getCapacity());
        courseDto.setCurrentEnrollment(course.getCurrentEnrollment());
        courseDto.setStartDate(course.getStartDate());
        courseDto.setEndDate(course.getEndDate());
        return courseDto;
    }
}
